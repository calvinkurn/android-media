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
import com.tokopedia.wishlistcollection.analytics.WishlistCollectionAnalytics
import com.tokopedia.wishlistcollection.data.params.UpdateWishlistCollectionNameParams
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionNamesResponse
import com.tokopedia.wishlistcollection.di.*
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.COLLECTION_ID
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.COLLECTION_NAME
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionDetailFragment
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionFragment
import com.tokopedia.wishlistcollection.view.viewmodel.BottomSheetUpdateWishlistCollectionNameViewModel
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.OK
import javax.inject.Inject

class BottomSheetUpdateWishlistCollectionName : BottomSheetUnify(), HasComponent<WishlistCollectionComponent> {
    private var binding by autoClearedNullable<BottomsheetCreateNewWishlistCollectionBinding>()
    private val userSession: UserSessionInterface by lazy { UserSession(activity) }
    private var listCollections: List<GetWishlistCollectionNamesResponse.GetWishlistCollectionNames.DataItem> = emptyList()
    private var newCollectionName = ""
    private var _existingCollectionName = ""
    private var actionListener: ActionListener? = null
    private val handler = Handler(Looper.getMainLooper())
    private val checkNameRunnable = Runnable {
        checkIsCollectionNameExists(newCollectionName)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val updateCollectionViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[BottomSheetUpdateWishlistCollectionNameViewModel::class.java]
    }

    companion object {
        private const val TAG: String = "AddToCollectionWishlistBottomSheet"
        const val REQUEST_CODE_LOGIN = 288
        private const val DELAY_CHECK_NAME = 500L

        @JvmStatic
        fun newInstance(collectionId: String, collectionName: String): BottomSheetUpdateWishlistCollectionName {
            return BottomSheetUpdateWishlistCollectionName().apply {
                val bundle = Bundle()
                bundle.putString(COLLECTION_ID, collectionId)
                bundle.putString(COLLECTION_NAME, collectionName)
                arguments = bundle
            }
        }
    }

    interface ActionListener {
        fun onSuccessUpdateCollectionName(message: String)
    }

    fun setListener(fragment: WishlistCollectionFragment) {
        this.actionListener = fragment
    }

    fun setListener(fragment: WishlistCollectionDetailFragment) {
        this.actionListener = fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _existingCollectionName = arguments?.getString(COLLECTION_NAME) ?: ""
        initInjector()
        checkLogin()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    init {
        showCloseIcon = true
        showHeader = true
        isFullpage = false
        isKeyboardOverlap = false
    }

    private fun initLayout() {
        binding = BottomsheetCreateNewWishlistCollectionBinding.inflate(LayoutInflater.from(context), null, false)
        binding?.run {
            collectionCreateNameInputTextField.editText.setText(_existingCollectionName)
            collectionCreateNameInputTextField.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    newCollectionName = p0.toString().trimStart().trimEnd()
                    if (newCollectionName.isNotEmpty()) {
                        handler.postDelayed(checkNameRunnable, DELAY_CHECK_NAME)
                    } else {
                        disableSaveButton()
                    }
                }
            })
            collectionCreateButton.apply {
                isEnabled = false
                text = getString(R.string.update_collection_bottomsheet_button)
            }
        }
        setChild(binding?.root)
        setTitle(getString(R.string.update_collection_bottomsheet_title))
    }

    private fun enableSaveButton() {
        binding?.run {
            collectionCreateButton.apply {
                text = getString(R.string.update_collection_bottomsheet_button)
                isEnabled = true
                setOnClickListener {
                    doUpdateWishlistCollectionName()
                    WishlistCollectionAnalytics.sendClickSimpanOnChangeCollectionNameBottomsheetEvent()
                }
            }
        }
    }

    private fun doUpdateWishlistCollectionName() {
        val collectionId = arguments?.getString(COLLECTION_ID) ?: ""
        val param = UpdateWishlistCollectionNameParams(collectionId = collectionId, collectionName = newCollectionName)
        updateCollectionViewModel.updateWishlistCollectionName(param)
    }

    private fun disableSaveButton() {
        binding?.run {
            collectionCreateButton.apply {
                text = getString(R.string.update_collection_bottomsheet_button)
                isEnabled = false
                setOnClickListener { }
            }
        }
    }

    private fun checkIsCollectionNameExists(checkName: String) {
        if (checkName.isEmpty()) {
            disableSaveButton()
        } else {
            if (listCollections.isNotEmpty()) {
                run check@{
                    listCollections.forEach { item ->
                        if (checkName.lowercase() == item.name.lowercase()) {
                            binding?.run {
                                if (checkName != _existingCollectionName) {
                                    collectionCreateNameInputTextField.isInputError = true
                                    val labelMessage = context?.getString(R.string.collection_create_bottomsheet_name_error) ?: ""
                                    collectionCreateNameInputTextField.setMessage(labelMessage)
                                }
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
        updateCollectionViewModel.getWishlistCollectionNames()
    }

    private fun initObserver() {
        observeCollectionNames()
        observeUpdateCollectionName()
    }

    private fun observeCollectionNames() {
        updateCollectionViewModel.collectionNames.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    if (result.data.status == OK) {
                        listCollections = result.data.data
                    } else {
                        val errorMessage = result.data.errorMessage.first().ifEmpty {
                            context?.getString(
                                R.string.wishlist_common_error_msg
                            )
                        }
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

    private fun observeUpdateCollectionName() {
        updateCollectionViewModel.updateWishlistCollectionNameResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    if (result.data.status == OK && result.data.data.success) {
                        actionListener?.onSuccessUpdateCollectionName(result.data.data.message)
                        dismiss()
                    } else {
                        val errorMessage = result.data.errorMessage.first().ifEmpty {
                            context?.getString(
                                R.string.wishlist_common_error_msg
                            )
                        }
                        errorMessage?.let { setTextFieldError(it) }
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    setTextFieldError(errorMessage)
                }
            }
        }
    }

    private fun setTextFieldError(errorMessage: String) {
        binding?.run {
            collectionCreateNameInputTextField.isInputError = true
            collectionCreateNameInputTextField.setMessage(errorMessage)
            disableSaveButton()
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

    override fun getComponent(): WishlistCollectionComponent {
        return DaggerWishlistCollectionComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .wishlistCollectionModule(WishlistCollectionModule(requireActivity()))
            .build()
    }

    override fun onPause() {
        handler.removeCallbacks(checkNameRunnable)
        super.onPause()
    }
}
