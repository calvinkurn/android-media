package com.tokopedia.wishlistcommon.view.bottomsheet

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.tokopedia.wishlist_common.R
import com.tokopedia.wishlist_common.databinding.BottomsheetAddWishlistCollectionBinding
import com.tokopedia.wishlistcommon.data.AddToWishlistCollectionTypeLayoutData
import com.tokopedia.wishlistcommon.data.response.GetWishlistCollectionsBottomSheetResponse
import com.tokopedia.wishlistcommon.di.AddToWishlistCollectionComponent
import com.tokopedia.wishlistcommon.di.DaggerAddToWishlistCollectionComponent
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.OK
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TYPE_COLLECTION_ADDITIONAL_SECTION
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TYPE_COLLECTION_ITEM
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TYPE_COLLECTION_MAIN_SECTION
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TYPE_CREATE_NEW_COLLECTION
import com.tokopedia.wishlistcommon.view.adapter.BottomSheetCollectionWishlistAdapter
import com.tokopedia.wishlistcommon.view.viewmodel.BottomSheetAddCollectionViewModel
import java.util.ArrayList
import javax.inject.Inject

class BottomSheetAddCollectionWishlist: BottomSheetUnify(), HasComponent<AddToWishlistCollectionComponent>,
    BottomSheetCollectionWishlistAdapter.ActionListener {
    private var binding by autoClearedNullable<BottomsheetAddWishlistCollectionBinding>()
    private val userSession: UserSessionInterface by lazy { UserSession(activity) }
    private lateinit var addToWishlistCollectionAdapter: BottomSheetCollectionWishlistAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val getCollectionsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[BottomSheetAddCollectionViewModel::class.java]
    }

    companion object {
        private const val TAG: String = "AddToCollectionWishlistBottomSheet"
        const val REQUEST_CODE_LOGIN = 288
        const val OPEN_WISHLIST_COLLECTION = "OPEN_WISHLIST_COLLECTION"

        @JvmStatic
        fun newInstance(): BottomSheetAddCollectionWishlist { return BottomSheetAddCollectionWishlist() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        checkLogin()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                loadData()
            } else {
                activity?.finish()
            }
        }
    }

    private fun checkLogin() {
        if (userSession.isLoggedIn) {
            loadData()
        } else {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareLayout()
        observingData()
    }

    private fun prepareLayout() {
        binding = BottomsheetAddWishlistCollectionBinding.inflate(LayoutInflater.from(context), null, false)
        binding?.run {
            rvAddWishlistCollection.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        showCloseIcon = false
        showHeader = true
        setChild(binding?.root)

        addToWishlistCollectionAdapter = BottomSheetCollectionWishlistAdapter().apply {
            setActionListener(this@BottomSheetAddCollectionWishlist)
        }
    }

    private fun loadData() {
        getCollectionsViewModel.getWishlistCollections()
    }

    private fun observingData() {
        observingGetCollections()
    }

    private fun observingGetCollections() {
        getCollectionsViewModel.collectionsBottomSheet.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    if (result.data.status == OK) {
                        val dataGetBottomSheetCollections = result.data.data
                        updateBottomSheet(dataGetBottomSheetCollections)
                        addToWishlistCollectionAdapter.addList(mapDataCollectionsBottomSheet(dataGetBottomSheetCollections))
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

    private fun updateBottomSheet(dataGetBottomSheetCollections: GetWishlistCollectionsBottomSheetResponse.Data.GetWishlistCollectionsBottomsheet.Data) {
        setTitle(dataGetBottomSheetCollections.title)
        setAction(dataGetBottomSheetCollections.titleButton.text) {
            if (dataGetBottomSheetCollections.titleButton.action == OPEN_WISHLIST_COLLECTION) {
                context?.let { c -> goToWishlistPage(c) }
            }
        }
    }

    private fun mapDataCollectionsBottomSheet(data: GetWishlistCollectionsBottomSheetResponse.Data.GetWishlistCollectionsBottomsheet.Data): ArrayList<AddToWishlistCollectionTypeLayoutData> {
        val listData = arrayListOf<AddToWishlistCollectionTypeLayoutData>()
        listData.add(AddToWishlistCollectionTypeLayoutData(data.mainSection.text, TYPE_COLLECTION_MAIN_SECTION))

        data.mainSection.collections.forEach { mainSectionItem ->
            listData.add(AddToWishlistCollectionTypeLayoutData(mainSectionItem, TYPE_COLLECTION_ITEM))
        }

        if (data.additionalSection.text.isNotEmpty()) {
            listData.add(AddToWishlistCollectionTypeLayoutData(data.additionalSection.text, TYPE_COLLECTION_ADDITIONAL_SECTION))
        }

        if (data.additionalSection.collections.isNotEmpty()) {
            data.additionalSection.collections.forEach { additionalItem ->
                listData.add(AddToWishlistCollectionTypeLayoutData(additionalItem, TYPE_COLLECTION_ITEM))
            }
        }

        if (data.placeholder.text.isNotEmpty()) {
            listData.add(AddToWishlistCollectionTypeLayoutData(data.placeholder, TYPE_CREATE_NEW_COLLECTION))
        }
        return listData
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

    private fun goToWishlistPage(context: Context) {
        RouteManager.route(context, ApplinkConst.NEW_WISHLIST)
    }

    override fun getComponent(): AddToWishlistCollectionComponent {
        return DaggerAddToWishlistCollectionComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun onCollectionItemClicked() {
        println("++ collection item is selected")
    }

    override fun onCreateNewCollectionClicked() {
        println("++ show bottomsheet create new collection")
    }
}