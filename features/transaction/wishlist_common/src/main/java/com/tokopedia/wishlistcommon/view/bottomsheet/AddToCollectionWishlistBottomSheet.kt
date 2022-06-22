package com.tokopedia.wishlistcommon.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
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
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlist_common.databinding.BottomsheetAddToWishlistCollectionBinding
import com.tokopedia.wishlistcommon.di.AddToWishlistCollectionComponent
import com.tokopedia.wishlistcommon.di.DaggerAddToWishlistCollectionComponent
import com.tokopedia.wishlistcommon.view.viewmodel.AddToWishlistCollectionBottomSheetViewModel
import javax.inject.Inject

class AddToCollectionWishlistBottomSheet: BottomSheetUnify(), HasComponent<AddToWishlistCollectionComponent> {
    private var binding by autoClearedNullable<BottomsheetAddToWishlistCollectionBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val getCollectionsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AddToWishlistCollectionBottomSheetViewModel::class.java]
    }

    companion object {
        private const val TAG: String = "AddToCollectionWishlistBottomSheet"

        @JvmStatic
        fun newInstance(): AddToCollectionWishlistBottomSheet { return AddToCollectionWishlistBottomSheet() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        loadData()
        binding = BottomsheetAddToWishlistCollectionBinding.inflate(LayoutInflater.from(context), null, false)
        /*binding?.run {
            rvAllWishlistCollections.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            }
        }*/
        showCloseIcon = false
        showHeader = true
        setChild(binding?.root)
        setTitle("Tersimpan di Wishlist!")
        setAction("Cek Wishlist") { context?.let { it1 -> goToWishlistPage(it1) } }
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
                    // lanjut di sini
                    if (result.data.status == OK) {
                        collectionAdapter.addList(mapCollection(result.data.data))
                    } else {
                        // TODO: show global error page?
                        val errorMessage = result.data.errorMessage.first().ifEmpty { context?.getString(
                            R.string.wishlist_v2_common_error_msg) }
                        errorMessage?.let { showToaster(it, "", Toaster.TYPE_ERROR) }
                    }
                }
                is Fail -> {
                    // TODO: show global error page?
                    finishRefresh()
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    showToaster(errorMessage, "", Toaster.TYPE_ERROR)
                }
            }
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
}