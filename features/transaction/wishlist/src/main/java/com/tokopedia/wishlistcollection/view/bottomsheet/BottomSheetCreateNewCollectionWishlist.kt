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
import com.tokopedia.wishlist.R as Rwishlist
import com.tokopedia.wishlist.databinding.BottomsheetCreateNewWishlistCollectionBinding
import com.tokopedia.wishlistcollection.analytics.WishlistCollectionAnalytics
import com.tokopedia.wishlistcollection.data.params.AddWishlistCollectionsHostBottomSheetParams
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionNamesResponse
import com.tokopedia.wishlistcollection.di.*
import com.tokopedia.wishlistcollection.view.bottomsheet.listener.ActionListenerFromCollectionPage
import com.tokopedia.wishlistcollection.view.bottomsheet.listener.ActionListenerFromPdp
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionDetailFragment
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionFragment
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionHostBottomSheetFragment
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.OK
import com.tokopedia.wishlistcollection.view.viewmodel.BottomSheetCreateNewCollectionViewModel
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.PRODUCT_IDs
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.SOURCE
import javax.inject.Inject

class BottomSheetCreateNewCollectionWishlist : BottomSheetUnify(),
    HasComponent<WishlistCollectionComponent> {
    private var binding by autoClearedNullable<BottomsheetCreateNewWishlistCollectionBinding>()
    private val userSession: UserSessionInterface by lazy { UserSession(activity) }
    private var listCollections: List<GetWishlistCollectionNamesResponse.GetWishlistCollectionNames.DataItem> =
        emptyList()
    private var newCollectionName = ""
    private var _productIds = arrayListOf<String>()
    private var _src = ""
    private var isSavingCreateNewCollection = false
    private var actionListenerFromPdp: ActionListenerFromPdp? = null
    private var actionListenerFromCollectionPage: ActionListenerFromCollectionPage? = null
    private val handler = Handler(Looper.getMainLooper())
    private val checkNameRunnable = Runnable {
        checkIsCollectionNameExists(newCollectionName)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val createNewCollectionViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[BottomSheetCreateNewCollectionViewModel::class.java]
    }

    companion object {
        private const val TAG: String = "AddToCollectionWishlistBottomSheet"
        const val REQUEST_CODE_LOGIN = 288
        private const val DELAY_CHECK_NAME = 500L

        @JvmStatic
        fun newInstance(productId: ArrayList<String>, source: String): BottomSheetCreateNewCollectionWishlist {
            return BottomSheetCreateNewCollectionWishlist().apply {
                val bundle = Bundle()
                bundle.putStringArrayList(PRODUCT_IDs, productId)
                bundle.putString(SOURCE, source)
                arguments = bundle
            }
        }
    }

    fun setListener(fragment: WishlistCollectionHostBottomSheetFragment) {
        this.actionListenerFromPdp = fragment
    }

    fun setListener(fragment: WishlistCollectionFragment) {
        this.actionListenerFromCollectionPage = fragment
    }

    fun setListener(fragment: WishlistCollectionDetailFragment) {
        this.actionListenerFromPdp = fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _productIds = arguments?.getStringArrayList(PRODUCT_IDs) ?: arrayListOf()
        _src = arguments?.getString(SOURCE) ?: ""
        initInjector()
        checkLogin()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initLayout()
        setBottomSheetCloseListener()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    init {
        showCloseIcon = true
        showHeader = true
        isFullpage = false
        isKeyboardOverlap = false
    }

    private fun initLayout() {
        binding = BottomsheetCreateNewWishlistCollectionBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        )
        binding?.run {
            collectionCreateNameInputTextField.editText.addTextChangedListener(object :
                TextWatcher {
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
            collectionCreateButton.isEnabled = false
        }
        setChild(binding?.root)
        setTitle(getString(Rwishlist.string.collection_create_bottomsheet_title))
    }

    private fun setBottomSheetCloseListener() {
        setCloseClickListener {
            dismiss()
            WishlistCollectionAnalytics.sendClickXOnCreateNewCollectionBottomSheetEvent()
        }
    }

    private fun enableSaveButton() {
        if (_productIds.isNotEmpty()) {
            binding?.run {
                collectionCreateButton.apply {
                    isEnabled = true
                    setOnClickListener { saveNewCollection(newCollectionName, _productIds) }
                }
            }
        } else {
            binding?.run {
                collectionCreateButton.apply {
                    isEnabled = true
                    setOnClickListener { createNewCollection(newCollectionName) }
                }
            }
        }
    }

    private fun disableSaveButton() {
        binding?.run {
            collectionCreateButton.apply {
                text = getText(Rwishlist.string.collection_create_bottomsheet_button_label)
                isEnabled = false
                setOnClickListener { }
            }
        }
    }

    private fun convertToSaveButton() {
        if (_productIds.isNotEmpty()) {
            binding?.run {
                collectionCreateButton.apply {
                    isEnabled = true
                    text = getString(Rwishlist.string.collection_save_to_existing_collection)
                    setOnClickListener {
                        saveNewCollection(newCollectionName, _productIds)
                    }
                }
            }
        }
    }

    private fun convertToCreateButton() {
        if (_productIds.isNotEmpty()) {
            binding?.run {
                collectionCreateButton.apply {
                    isEnabled = true
                    text = getString(Rwishlist.string.collection_create_bottomsheet_button_label)
                    setOnClickListener {
                        saveNewCollection(newCollectionName, _productIds, true)
                    }
                }
            }
        }
    }

    private fun createNewCollection(collectionName: String) {
        createNewCollectionViewModel.createNewWishlistCollection(collectionName)
    }

    private fun saveNewCollection(collectionName: String, productIds: List<String>, isSavingCreateNewCollection: Boolean = false) {
        this.isSavingCreateNewCollection = isSavingCreateNewCollection
        val param = AddWishlistCollectionsHostBottomSheetParams(
            productIds = productIds,
            collectionName = collectionName
        )
        createNewCollectionViewModel.saveNewWishlistCollection(param)
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
                                collectionCreateNameInputTextField.isInputError = _productIds.isEmpty()

                                val labelMessage =
                                    getString(Rwishlist.string.collection_create_bottomsheet_name_error)
                                collectionCreateNameInputTextField.setMessage(labelMessage)

                                if (_productIds.isNotEmpty()) {
                                    convertToSaveButton()
                                } else {
                                    disableSaveButton()
                                }
                                return@check
                            }
                        } else {
                            binding?.run {
                                collectionCreateNameInputTextField.isInputError = false
                                collectionCreateNameInputTextField.setMessage("")

                                if (_productIds.isNotEmpty()) {
                                    convertToCreateButton()
                                } else {
                                    enableSaveButton()
                                }
                            }
                        }
                    }
                }
            } else {
                if (checkName.isNotEmpty()) enableSaveButton()
                else disableSaveButton()
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
            startActivityForResult(
                RouteManager.getIntent(context, ApplinkConst.LOGIN),
                REQUEST_CODE_LOGIN
            )
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
        observeCreateCollection()
    }

    private fun observeCollectionNames() {
        createNewCollectionViewModel.collectionNames.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    if (result.data.status == OK) {
                        listCollections = result.data.data
                    } else {
                        val errorMessage = result.data.errorMessage.first().ifEmpty {
                            getString(
                                Rwishlist.string.wishlist_common_error_msg
                            )
                        }
                        showToaster(errorMessage, "", Toaster.TYPE_ERROR)
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
                    if (result.data.status == OK && result.data.dataItem.success) {
                        actionListenerFromPdp?.onSuccessSaveToNewCollection(result.data.dataItem)
                        if (isSavingCreateNewCollection) {
                            WishlistCollectionAnalytics.sendClickBuatKoleksiOnCreateNewCollectionBottomsheetEvent(
                                result.data.dataItem.collectionId,
                                _src
                            )
                        }
                        dismiss()
                    } else {
                        val errorMessage = if (result.data.errorMessage.isNotEmpty()) {
                            result.data.errorMessage.firstOrNull() ?: ""
                        } else if (result.data.dataItem.message.isNotEmpty()) {
                            result.data.dataItem.message
                        } else {
                            getString(com.tokopedia.wishlist.R.string.wishlist_v2_common_error_msg)
                        }
                        actionListenerFromPdp?.onFailedSaveToNewCollection(errorMessage)
                        dismiss()
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    actionListenerFromPdp?.onFailedSaveToNewCollection(errorMessage)
                    dismiss()
                }
            }
        }
    }

    private fun observeCreateCollection() {
        createNewCollectionViewModel.createWishlistCollectionResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    if (result.data.status == OK && result.data.dataCreate.success) {
                        actionListenerFromCollectionPage?.onSuccessCreateNewCollection(
                            result.data.dataCreate,
                            newCollectionName
                        )
                        WishlistCollectionAnalytics.sendClickBuatKoleksiOnCreateNewCollectionBottomsheetEvent(
                            result.data.dataCreate.id,
                            _src
                        )
                        dismiss()
                    } else {
                        val errorMessage = result.data.errorMessage.first().ifEmpty {
                            getString(Rwishlist.string.wishlist_common_error_msg)
                        }
                        setTextFieldError(errorMessage)
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