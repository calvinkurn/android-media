package com.tokopedia.shop.open.shop_open_revamp.presentation.view.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.shop.open.R
import com.tokopedia.shop.open.shop_open_revamp.common.ImageAssets.IMG_SHOP_OPEN_SPLASH_SCREEN
import com.tokopedia.shop.open.shop_open_revamp.common.PageNameConstant
import com.tokopedia.shop.open.shop_open_revamp.listener.FragmentNavigationInterface
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_shop_open_revamp_finish.*
import kotlinx.android.synthetic.main.fragment_shop_open_revamp_finish.txt_greeting
import kotlinx.android.synthetic.main.fragment_shop_open_revamp_splash_screen.*

class ShopOpenRevampSplashScreenFragment : Fragment() {

    private val handler = Handler()
    lateinit var fragmentNavigationInterface: FragmentNavigationInterface
    private lateinit var imageViewShopCreated: ImageView

    private val userSession: UserSessionInterface by lazy {
        UserSession(activity)
    }

    companion object {
        const val SECOND_FRAGMENT_TAG = "second"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentNavigationInterface = context as FragmentNavigationInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_shop_open_revamp_splash_screen, container, false)
        imageViewShopCreated = view.findViewById(R.id.img_icon_open_shop_finish)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupIconImage(view)

        val userName = userSession.name
        val firstName = userName.substring(0, userName.indexOf(" "))
        val greetingText = "Hore ${firstName}!"
        txt_greeting.text = greetingText
        handler.postDelayed({
            fragmentNavigationInterface
                    .navigateToNextPage(PageNameConstant.QUISIONER_PAGE, SECOND_FRAGMENT_TAG)
        }, 3000)
    }

    private fun setupIconImage(view: View) {
        ImageHandler.loadImage(
                view.context,
                imageViewShopCreated,
                IMG_SHOP_OPEN_SPLASH_SCREEN,
                null
        )
    }

}