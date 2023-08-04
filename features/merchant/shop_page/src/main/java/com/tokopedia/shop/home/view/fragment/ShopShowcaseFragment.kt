package com.tokopedia.shop.home.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.databinding.FragmentShopShowcaseBinding
import com.tokopedia.shop.home.di.component.DaggerShopPageHomeComponent
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseUiModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlin.collections.ArrayList

open class ShopShowcaseFragment : BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_SHOWCASES = "showcases"

        @JvmStatic
        fun newInstance(
            showcases: List<ShopHomeShowcaseUiModel.ShopHomeShowCaseTab.ShopHomeShowcase>
        ): ShopShowcaseFragment {
            return ShopShowcaseFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(BUNDLE_KEY_SHOWCASES, ArrayList(showcases))
                }
            }
        }

    }

    private val showcases by lazy {
        arguments?.getParcelableArrayList<ShopHomeShowcaseUiModel.ShopHomeShowCaseTab.ShopHomeShowcase>(
            BUNDLE_KEY_SHOWCASES
        )?.toList().orEmpty()
    }


    private var binding by autoClearedNullable<FragmentShopShowcaseBinding>()

    override fun getScreenName(): String = ShopShowcaseFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        activity?.run {
            DaggerShopPageHomeComponent
                .builder()
                .shopPageHomeModule(ShopPageHomeModule())
                .shopComponent(ShopComponentHelper().getComponent(application, this))
                .build()
                .inject(this@ShopShowcaseFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopShowcaseBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()
        renderShowcase(showcases)
    }

    private fun renderShowcase(
        showcases: List<ShopHomeShowcaseUiModel.ShopHomeShowCaseTab.ShopHomeShowcase>
    ) {
        val firstShowcase = showcases.getOrNull(0)
        val secondShowcase = showcases.getOrNull(1)
        val thirdShowcase = showcases.getOrNull(2)
        val fourthShowcase = showcases.getOrNull(3)
        val fifthShowcase = showcases.getOrNull(4)


        firstShowcase?.let {
            binding?.imgFirstBanner?.loadImage(firstShowcase.imageUrl)
            binding?.tpgFirstBannerTitle?.text = firstShowcase.name
            binding?.imgFirstBanner?.visible()
            binding?.tpgFirstBannerTitle?.visible()
        }

        secondShowcase?.let {
            binding?.imgSecondBanner?.loadImage(secondShowcase.imageUrl)
            binding?.tpgSecondBannerTitle?.text = secondShowcase.name
            binding?.imgSecondBanner?.visible()
            binding?.tpgSecondBannerTitle?.visible()
        }
        thirdShowcase?.let {
            binding?.imgThirdBanner?.loadImage(thirdShowcase.imageUrl)
            binding?.tpgThirdBannerTitle?.text = thirdShowcase.name
            binding?.imgThirdBanner?.visible()
            binding?.tpgThirdBannerTitle?.visible()
        }
        fourthShowcase?.let {
            binding?.imgFourthBanner?.loadImage(fourthShowcase.imageUrl)
            binding?.tpgFourthBannerTitle?.text = fourthShowcase.name
            binding?.imgFourthBanner?.visible()
            binding?.tpgFourthBannerTitle?.visible()
        }
        fifthShowcase?.let {
            binding?.imgFifthBanner?.loadImage(fifthShowcase.imageUrl)
            binding?.tpgFifthBannerTitle?.text = fifthShowcase.name
            binding?.imgFifthBanner?.visible()
            binding?.tpgFifthBannerTitle?.visible()
        }
    }


}
