package com.tokopedia.shop.info.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.databinding.FragmentShopInfoReimagineBinding
import com.tokopedia.shop.info.di.component.DaggerShopInfoComponent
import com.tokopedia.shop.info.di.module.ShopInfoModule
import com.tokopedia.shop.info.domain.entity.ShopRatingAndReviews
import com.tokopedia.shop.info.view.viewmodel.ShopInfoReimagineViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ShopInfoReimagineFragment : BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_SHOP_ID = "shopId"
        fun newInstance(shopId: String): ShopInfoReimagineFragment {
            return ShopInfoReimagineFragment().apply {
                arguments = Bundle().apply {
                    putString(BUNDLE_KEY_SHOP_ID, shopId)
                }
            }
        }

    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession : UserSessionInterface
    private var binding by autoClearedNullable<FragmentShopInfoReimagineBinding>()
    
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider[ShopInfoReimagineViewModel::class.java] }
    private val shopId by lazy { arguments?.getString(BUNDLE_KEY_SHOP_ID).orEmpty() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopInfoReimagineBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeShopRatingAndReview()
        viewModel.getShopRating(shopId)
    }

    override fun getScreenName(): String = ShopInfoReimagineFragment::class.java.simpleName


    override fun initInjector() {
        DaggerShopInfoComponent.builder().shopInfoModule(ShopInfoModule())
            .shopComponent(getComponent(ShopComponent::class.java))
            .build()
            .inject(this)
    }
    
    

    private fun observeShopRatingAndReview() {
        viewModel.shopRatingAndReview.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> showShopRatingAndReview(result.data)
                is Fail -> {}
            }
        }
    }

    private fun showShopRatingAndReview(data: ShopRatingAndReviews) {
        binding?.tpgReview?.text = data.rating.rating.positivePercentageFmt
    }
    

}
