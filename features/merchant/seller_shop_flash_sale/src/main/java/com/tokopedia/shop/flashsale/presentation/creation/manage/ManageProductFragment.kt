package com.tokopedia.shop.flashsale.presentation.creation.manage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentManageProductBinding
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.enums.PageMode
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ManageProductFragment : BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_PAGE_MODE = "page_mode"
        private const val SECOND_STEP = 2
        private const val EMPTY_STATE_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/flash-sale-toko/ic_no_active_campaign.png"

        @JvmStatic
        fun newInstance(): ManageProductFragment {
            return ManageProductFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_KEY_PAGE_MODE, pageMode)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ManageProductViewModel::class.java) }
    private var binding by autoClearedNullable<SsfsFragmentManageProductBinding>()
    private val pageMode: PageMode? by lazy { arguments?.getParcelable(BUNDLE_KEY_PAGE_MODE) }

    override fun getScreenName(): String = ManageProductFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsfsFragmentManageProductBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeProductList()
        viewModel.getProducts(0,0)
    }

    private fun setupView(){
        binding?.apply {
            header.headerSubTitle = String.format(
                getString(R.string.sfs_placeholder_step_counter),
                SECOND_STEP
            )
        }
    }

    private fun observeProductList(){
        viewModel.products.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> {

                }
                is Fail -> {

                }
             }
        }
    }

    private fun showEmptyState(){
        binding?.apply {
            emptyState.visible()
            cardIncompleteProductInfo.gone()
            tpgProductCount.gone()
            tpgAddProduct.gone()
            recyclerViewProduct.gone()
            cardBottomButtonGroup.gone()

            emptyState.setImageUrl(EMPTY_STATE_IMAGE_URL)
            emptyState.setOnClickListener {

            }
        }
    }

    private fun hideEmptyState(){
        binding?.apply {
            emptyState.gone()
        }
    }
}