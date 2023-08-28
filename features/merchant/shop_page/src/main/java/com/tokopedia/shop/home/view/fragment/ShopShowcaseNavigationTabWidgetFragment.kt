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
            binding?.imgMainShowcase?.loadImage(mainShowcase.imageUrl)
            binding?.imgMainShowcaseTitle?.text = mainShowcase.name

            binding?.imgMainShowcase?.visible()
            binding?.imgMainShowcaseTitle?.visible()

            binding?.imgMainShowcase?.setOnClickListener { onShowcaseClick(mainShowcase) }
            binding?.imgMainShowcaseTitle?.setOnClickListener { onShowcaseClick(mainShowcase) }
        }

        firstShowcase?.let {
            binding?.imgFirstShowcase?.loadImage(firstShowcase.imageUrl)
            binding?.imgFirstShowcaseTitle?.text = firstShowcase.name

            binding?.imgFirstShowcase?.visible()
            binding?.imgFirstShowcaseTitle?.visible()

            binding?.imgFirstShowcase?.setOnClickListener { onShowcaseClick(firstShowcase) }
            binding?.imgFirstShowcaseTitle?.setOnClickListener { onShowcaseClick(firstShowcase) }
        }

        secondShowcase?.let {
            binding?.imgSecondShowcase?.loadImage(secondShowcase.imageUrl)
            binding?.tpgSecondShowcaseTitle?.text = secondShowcase.name

            binding?.imgSecondShowcase?.visible()
            binding?.tpgSecondShowcaseTitle?.visible()

            binding?.imgSecondShowcase?.setOnClickListener { onShowcaseClick(secondShowcase) }
            binding?.tpgSecondShowcaseTitle?.setOnClickListener { onShowcaseClick(secondShowcase) }
        }

        thirdShowcase?.let {
            binding?.imgThirdShowcase?.loadImage(thirdShowcase.imageUrl)
            binding?.tpgThirdShowcaseTitle?.text = thirdShowcase.name

            binding?.imgThirdShowcase?.visible()
            binding?.tpgThirdShowcaseTitle?.visible()

            binding?.imgThirdShowcase?.setOnClickListener { onShowcaseClick(thirdShowcase) }
            binding?.tpgThirdShowcaseTitle?.setOnClickListener { onShowcaseClick(thirdShowcase) }
        }

        fourthShowcase?.let {
            binding?.imgFourthShowcase?.loadImage(fourthShowcase.imageUrl)
            binding?.imgFourthShowcaseTitle?.text = fourthShowcase.name

            binding?.imgFourthShowcase?.visible()
            binding?.imgFourthShowcaseTitle?.visible()

            binding?.imgFourthShowcase?.setOnClickListener { onShowcaseClick(fourthShowcase) }
            binding?.imgFourthShowcaseTitle?.setOnClickListener { onShowcaseClick(fourthShowcase) }
        }
    }

    fun setOnShowcaseClick(onShowcaseClick: (Showcase) -> Unit) {
        this.onShowcaseClick = onShowcaseClick
    }

}
