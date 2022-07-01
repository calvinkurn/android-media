package com.tokopedia.wishlistcollection.view.bottomsheet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import com.tokopedia.wishlistcollection.di.CreateWishlistCollectionComponent
import com.tokopedia.wishlistcollection.di.DaggerCreateWishlistCollectionComponent
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.OK
import com.tokopedia.wishlistcollection.view.viewmodel.BottomSheetCreateNewCollectionViewModel
import javax.inject.Inject

class BottomSheetCreateNewCollectionWishlist: BottomSheetUnify(), HasComponent<CreateWishlistCollectionComponent> {
    private var binding by autoClearedNullable<BottomsheetCreateNewWishlistCollectionBinding>()
    private val userSession: UserSessionInterface by lazy { UserSession(activity) }
    private var listCollections: List<GetWishlistCollectionNamesResponse.Data.GetWishlistCollectionNames.DataItem> = emptyList()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val createNewCollectionViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[BottomSheetCreateNewCollectionViewModel::class.java]
    }

    companion object {
        private const val TAG: String = "AddToCollectionWishlistBottomSheet"
        const val REQUEST_CODE_LOGIN = 288
        const val OPEN_WISHLIST_COLLECTION = "OPEN_WISHLIST_COLLECTION"

        @JvmStatic
        fun newInstance() = BottomSheetCreateNewCollectionWishlist()
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
        initLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initLayout() {
        binding = BottomsheetCreateNewWishlistCollectionBinding.inflate(LayoutInflater.from(context), null, false)
        showCloseIcon = true
        showHeader = true
        setTitle(getString(R.string.collection_create_bottomsheet_title))
        setChild(binding?.root)
        initInputText()
    }

    private fun initInputText() {
        binding?.run {
            collectionCreateNameInputTextField.editText.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    println("++ onTextChanged - text = ${p0.toString()}")
                }

                override fun afterTextChanged(p0: Editable?) {
                    println("++ afterTextChanged - text = ${p0.toString()}")
                }

            })
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
        createNewCollectionViewModel.collectionNames.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    if (result.data.status == OK) {
                        val dataGetBottomSheetCollections = result.data.data
                        listCollections = result.data.data
                        if (listCollections.isNotEmpty()) {
                            listCollections.forEach {
                                println("++ ${it.name}")
                            }
                        }
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

    override fun getComponent(): CreateWishlistCollectionComponent {
        return DaggerCreateWishlistCollectionComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }
}