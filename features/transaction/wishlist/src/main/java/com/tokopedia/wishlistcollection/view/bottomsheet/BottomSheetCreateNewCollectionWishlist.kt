package com.tokopedia.wishlistcollection.view.bottomsheet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.databinding.BottomsheetCreateNewWishlistCollectionBinding
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionNamesResponse
import com.tokopedia.wishlistcollection.di.BottomSheetCreateWishlistCollectionComponent
import com.tokopedia.wishlistcollection.di.BottomSheetCreateWishlistCollectionModule
import com.tokopedia.wishlistcollection.di.DaggerBottomSheetCreateWishlistCollectionComponent
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionHostBottomSheetFragment
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.OK
import com.tokopedia.wishlistcollection.view.viewmodel.BottomSheetCreateNewCollectionViewModel
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.PRODUCT_IDs
import javax.inject.Inject

class BottomSheetCreateNewCollectionWishlist: BottomSheetUnify(), HasComponent<BottomSheetCreateWishlistCollectionComponent> {
    private var binding by autoClearedNullable<BottomsheetCreateNewWishlistCollectionBinding>()
    private val userSession: UserSessionInterface by lazy { UserSession(activity) }
    private var listCollections: List<GetWishlistCollectionNamesResponse.Data.GetWishlistCollectionNames.DataItem> = emptyList()
    private var newCollectionName = ""
    private var actionListener: ActionListener? = null
    private val handler = Handler(Looper.getMainLooper())
    private val checkNameRunnable = Runnable {
        checkIsCollectionNameExists(newCollectionName)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val createNewCollectionViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[BottomSheetCreateNewCollectionViewModel::class.java]
    }

    companion object {
        private const val TAG: String = "AddToCollectionWishlistBottomSheet"
        const val REQUEST_CODE_LOGIN = 288
        const val OPEN_WISHLIST_COLLECTION = "OPEN_WISHLIST_COLLECTION"
        private const val DELAY_CHECK_NAME = 500L

        @JvmStatic
        fun newInstance(productId: String): BottomSheetCreateNewCollectionWishlist {
            return BottomSheetCreateNewCollectionWishlist().apply {
                val bundle = Bundle()
                bundle.putString(PRODUCT_IDs, productId)
                arguments = bundle
            }
        }
    }

    interface ActionListener {
        fun onSuccessSaveToNewCollection(message: String)
        fun onFailedSaveToNewCollection(errorMessage: String?)
    }

    fun setListener(fragment: WishlistCollectionHostBottomSheetFragment) {
        this.actionListener = fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        checkLogin()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupBottomSheet() {
        binding = BottomsheetCreateNewWishlistCollectionBinding.inflate(LayoutInflater.from(context), null, false)
        binding?.run {
            collectionCreateNameInputTextField.editText.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    newCollectionName = p0.toString()
                    if (newCollectionName.isNotEmpty()) {
                        handler.postDelayed(checkNameRunnable, DELAY_CHECK_NAME)
                    }
                }

            })
            collectionCreateButton.isEnabled = false
        }
        showCloseIcon = true
        showHeader = true
        isFullpage = false
        isKeyboardOverlap = false
        setChild(binding?.root)
        setTitle(getString(R.string.collection_create_bottomsheet_title))
    }

    private fun enableSaveButton() {
        val arrayProductIds = arrayListOf<String>()
        arguments?.getString(PRODUCT_IDs)?.let { arrayProductIds.add(it) }
        binding?.run {
            collectionCreateButton.apply {
                isEnabled = true
                setOnClickListener { saveNewCollection(newCollectionName, arrayProductIds) }
            }
        }
    }

    private fun disableSaveButton() {
        binding?.run {
            collectionCreateButton.apply {
                isEnabled = false
                setOnClickListener {  }
            }
        }
    }

    private fun saveNewCollection(collectionName: String, productIds: List<String>) {
        createNewCollectionViewModel.saveNewWishlistCollection(collectionName, productIds)
    }

    private fun checkIsCollectionNameExists(checkName: String) {
        if (listCollections.isNotEmpty()) {
            run check@ {
                listCollections.forEach { item ->
                    if (checkName == item.name) {
                        binding?.run {
                            collectionCreateNameInputTextField.isInputError = true

                            val labelMessage = context?.getString(R.string.collection_create_bottomsheet_name_error) ?: ""
                            collectionCreateNameInputTextField.setMessage(labelMessage)
                            disableSaveButton()
                            return@check
                        }
                    } else {
                        binding?.run {
                            collectionCreateNameInputTextField.isInputError = false
                            collectionCreateNameInputTextField.setMessage("")
                            enableSaveButton()
                        }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                getWishlistCollectionNames()
            } else {
                activity?.finish()
            }
        }
    }

    private fun checkLogin() {
        if (userSession.isLoggedIn) {
            getWishlistCollectionNames()
        } else {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
    }

    private fun getWishlistCollectionNames() {
        createNewCollectionViewModel.getWishlistCollectionNames()
    }

    private fun initObserver() {
        observeCollectionNames()
        observeAddCollectionItem()
    }

    private fun observeCollectionNames() {
        createNewCollectionViewModel.collectionNames.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    if (result.data.status == OK) {
                        listCollections = result.data.data
                    } else {
                        val errorMessage = result.data.errorMessage.first().ifEmpty { context?.getString(
                            R.string.wishlist_common_error_msg) }
                        errorMessage?.let { showToaster(it, "", Toaster.TYPE_ERROR) }
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToaster(errorMessage, "", Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun observeAddCollectionItem() {
        createNewCollectionViewModel.addWishlistCollectionItem.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    if (result.data.status == OK) {
                        actionListener?.onSuccessSaveToNewCollection(result.data.dataItem.message)
                        dismiss()
                    } else {
                        val errorMessage = result.data.errorMessage.first().ifEmpty { context?.getString(
                            R.string.wishlist_common_error_msg) }
                        actionListener?.onFailedSaveToNewCollection(errorMessage)
                        dismiss()
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    actionListener?.onFailedSaveToNewCollection(errorMessage)
                    dismiss()
                }
            }
        }
    }

    private fun showToaster(message: String, actionText: String, type: Int) {
        val toasterSuccess = Toaster
        view?.let { v ->
            toasterSuccess.build(v, message, Toaster.LENGTH_LONG, type, actionText).show()
        }
    }

    private fun initInjector() {
        component.inject(this)
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    override fun getComponent(): BottomSheetCreateWishlistCollectionComponent {
        return DaggerBottomSheetCreateWishlistCollectionComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .bottomSheetCreateWishlistCollectionModule(BottomSheetCreateWishlistCollectionModule())
            .build()
    }

    override fun onPause() {
        handler.removeCallbacks(checkNameRunnable)
        super.onPause()
    }
}