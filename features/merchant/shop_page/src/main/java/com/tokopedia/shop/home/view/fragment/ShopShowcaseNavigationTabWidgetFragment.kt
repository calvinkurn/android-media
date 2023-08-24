package com.tokopedia.shop.home.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.databinding.FragmentShopShowcaseNavigationTabWidgetBinding
import com.tokopedia.shop.home.di.component.DaggerShopPageHomeComponent
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.view.model.showcase_navigation.Showcase
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlin.collections.ArrayList

class ShopShowcaseNavigationTabWidgetFragment : BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_SHOWCASES = "showcases"
        private const val FIRST_SHOWCASE_INDEX = 0
        private const val SECOND_SHOWCASE_INDEX = 1
        private const val THIRD_SHOWCASE_INDEX = 2
        private const val FOURTH_SHOWCASE_INDEX = 3
        private const val FIFTH_SHOWCASE_INDEX = 4

        @JvmStatic
        fun newInstance(
            showcases: List<Showcase>
        ): ShopShowcaseNavigationTabWidgetFragment {
            return ShopShowcaseNavigationTabWidgetFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(BUNDLE_KEY_SHOWCASES, ArrayList(showcases))
                }
            }
        }

    }

    private val showcases by lazy {
        arguments?.getParcelableArrayList<Showcase>(BUNDLE_KEY_SHOWCASES)?.toList().orEmpty()
    }

    private var onShowcaseClick : (Showcase) -> Unit = {}

    private var binding by autoClearedNullable<FragmentShopShowcaseNavigationTabWidgetBinding>()

    override fun getScreenName(): String = ShopShowcaseNavigationTabWidgetFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        activity?.run {
            DaggerShopPageHomeComponent
                .builder()
                .shopPageHomeModule(ShopPageHomeModule())
                .shopComponent(ShopComponentHelper().getComponent(application, this))
                .build()
                .inject(this@ShopShowcaseNavigationTabWidgetFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopShowcaseNavigationTabWidgetBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderShowcase(showcases)
    }

    private fun renderShowcase(
        showcases: List<Showcase>
    ) {
        val mainShowcase = showcases.getOrNull(FIRST_SHOWCASE_INDEX)
        val firstShowcase = showcases.getOrNull(SECOND_SHOWCASE_INDEX)
        val secondShowcase = showcases.getOrNull(THIRD_SHOWCASE_INDEX)
        val thirdShowcase = showcases.getOrNull(FOURTH_SHOWCASE_INDEX)
        val fourthShowcase = showcases.getOrNull(FIFTH_SHOWCASE_INDEX)

        mainShowcase?.let {
            binding?.imgMainBanner?.loadImage(mainShowcase.imageUrl)
            binding?.imgMainBannerTitle?.text = mainShowcase.name

            binding?.imgMainBanner?.visible()
            binding?.imgMainBannerTitle?.visible()

            binding?.imgMainBanner?.setOnClickListener { onShowcaseClick(mainShowcase) }
            binding?.imgMainBannerTitle?.setOnClickListener { onShowcaseClick(mainShowcase) }
        }

        firstShowcase?.let {
            binding?.imgFirstBanner?.loadImage(firstShowcase.imageUrl)
            binding?.imgFirstBannerTitle?.text = firstShowcase.name

            binding?.imgFirstBanner?.visible()
            binding?.imgFirstBannerTitle?.visible()

            binding?.imgFirstBanner?.setOnClickListener { onShowcaseClick(firstShowcase) }
            binding?.imgFirstBannerTitle?.setOnClickListener { onShowcaseClick(firstShowcase) }
        }

        secondShowcase?.let {
            binding?.imgSecondBanner?.loadImage(secondShowcase.imageUrl)
            binding?.tpgSecondBannerTitle?.text = secondShowcase.name

            binding?.imgSecondBanner?.visible()
            binding?.tpgSecondBannerTitle?.visible()

            binding?.imgSecondBanner?.setOnClickListener { onShowcaseClick(secondShowcase) }
            binding?.tpgSecondBannerTitle?.setOnClickListener { onShowcaseClick(secondShowcase) }
        }

        thirdShowcase?.let {
            binding?.imgThirdBanner?.loadImage(thirdShowcase.imageUrl)
            binding?.tpgThirdBannerTitle?.text = thirdShowcase.name

            binding?.imgThirdBanner?.visible()
            binding?.tpgThirdBannerTitle?.visible()

            binding?.imgThirdBanner?.setOnClickListener { onShowcaseClick(thirdShowcase) }
            binding?.tpgThirdBannerTitle?.setOnClickListener { onShowcaseClick(thirdShowcase) }
        }

        fourthShowcase?.let {
            binding?.imgFourthBanner?.loadImage(fourthShowcase.imageUrl)
            binding?.imgFourthBannerTitle?.text = fourthShowcase.name

            binding?.imgFourthBanner?.visible()
            binding?.imgFourthBannerTitle?.visible()

            binding?.imgFourthBanner?.setOnClickListener { onShowcaseClick(fourthShowcase) }
            binding?.imgFourthBannerTitle?.setOnClickListener { onShowcaseClick(fourthShowcase) }
        }
    }

    fun setOnShowcaseClick(onShowcaseClick: (Showcase) -> Unit) {
        this.onShowcaseClick = onShowcaseClick
    }

}
