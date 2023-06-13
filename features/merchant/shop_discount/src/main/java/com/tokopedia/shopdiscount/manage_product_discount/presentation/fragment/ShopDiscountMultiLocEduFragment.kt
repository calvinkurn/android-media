package com.tokopedia.shopdiscount.manage_product_discount.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.header.HeaderUnify
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.FragmentManageProductDiscountMultiLocBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ShopDiscountMultiLocEduFragment : BaseDaggerFragment() {

    companion object {
        const val MODE_ARG = "mode_arg"
        private const val URL_IMAGE_SELLER_EDU =
            "https://images.tokopedia.net/img/android/shop-discount/multi_loc_seller_edu.png"

        fun createInstance(
            mode: String
        ) = ShopDiscountMultiLocEduFragment().apply {
            arguments = Bundle().apply {
                putString(ShopDiscountManageProductFragment.MODE_ARG, mode)
            }
        }
    }

    private var viewBinding by autoClearedNullable<FragmentManageProductDiscountMultiLocBinding>()
    override fun getScreenName(): String =
        ShopDiscountMultiLocEduFragment::class.java.canonicalName.orEmpty()

    private var mode: String = ""
    private var headerUnify: HeaderUnify? = null
    private var imageSellerEdu: ImageUnify? = null

    override fun initInjector() {
        DaggerShopDiscountComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding =
            FragmentManageProductDiscountMultiLocBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgumentsData()
        initView()
        setupToolbar()
        setupImageUnify()
    }

    private fun setupImageUnify() {
        imageSellerEdu?.loadImage(URL_IMAGE_SELLER_EDU)
    }

    private fun getArgumentsData() {
        arguments?.let {
            mode = it.getString(MODE_ARG).orEmpty()
        }
    }

    private fun initView() {
        viewBinding?.let {
            headerUnify = it.headerUnify
            imageSellerEdu = it.imageSellerEdu
        }
    }

    private fun setupToolbar() {
        headerUnify?.apply {
            title = when (mode) {
                ShopDiscountManageDiscountMode.CREATE -> {
                    getString(R.string.shop_discount_manage_product_discount_multi_loc_manage_toolbar_title)
                }
                ShopDiscountManageDiscountMode.UPDATE -> {
                    getString(R.string.shop_discount_manage_product_discount_multi_loc_edit_toolbar_title)
                }
                else -> {
                    ""
                }
            }
            setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun onBackPressed() {
        activity?.finish()
    }

}
