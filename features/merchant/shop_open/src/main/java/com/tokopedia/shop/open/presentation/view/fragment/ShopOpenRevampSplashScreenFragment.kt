package com.tokopedia.shop.open.presentation.view.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.header.HeaderUnify
import com.tokopedia.shop.open.R
import com.tokopedia.shop.open.analytic.ShopOpenRevampTracking
import com.tokopedia.shop.open.common.EspressoIdlingResource
import com.tokopedia.shop.open.common.ImageAssets.IMG_SHOP_OPEN_SPLASH_SCREEN
import com.tokopedia.shop.open.common.PageNameConstant
import com.tokopedia.shop.open.common.ScreenNameTracker
import com.tokopedia.shop.open.listener.FragmentNavigationInterface
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

class ShopOpenRevampSplashScreenFragment : Fragment() {

    private val handler = Handler()
    private lateinit var imageViewShopCreated: ImageUnify
    private var shopOpenRevampTracking: ShopOpenRevampTracking? = null
    private var fragmentNavigationInterface: FragmentNavigationInterface? = null
    private var txtGreeting: Typography? = null

    private val userSession: UserSessionInterface by lazy {
        UserSession(activity)
    }

    companion object {
        const val SECOND_FRAGMENT_TAG = "second"
        const val DELAY_TIMER_IN_MILISECOND = 3000L
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentNavigationInterface = context as FragmentNavigationInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            shopOpenRevampTracking = ShopOpenRevampTracking(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_shop_open_revamp_splash_screen, container, false)
        imageViewShopCreated = view.findViewById(R.id.img_icon_open_shop_finish)
        txtGreeting = view.findViewById(R.id.txt_greeting)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupIconImage(imageViewShopCreated)

        setupToolbarActions(view)
        val fullName = userSession.name
        val firstName = fullName.split(" ")[0]
        val greetingText = getString(R.string.open_shop_revamp_text_horay_name, firstName)
        txtGreeting?.text = greetingText
        shopOpenRevampTracking?.sendScreenNameTracker(ScreenNameTracker.SCREEN_HOORAY)
        handler.postDelayed({
            context?.let{
                fragmentNavigationInterface
                    ?.navigateToNextPage(PageNameConstant.QUISIONER_PAGE, SECOND_FRAGMENT_TAG)
            }
            EspressoIdlingResource.decrement()
        }, DELAY_TIMER_IN_MILISECOND)
    }

    private fun setupIconImage(imageViewShopCreated: ImageUnify) {
        imageViewShopCreated.setImageUrl(IMG_SHOP_OPEN_SPLASH_SCREEN)
    }

    private fun setupToolbarActions(view: View?) {
        view?.findViewById<HeaderUnify>(R.id.toolbar_splash_screen)?.apply {
            transparentMode = context.isDarkMode()
            isShowShadow = false
            setNavigationOnClickListener {
                activity?.finish()
            }
        }
    }
}
