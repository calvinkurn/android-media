package com.tokopedia.shop.open.shop_open_revamp.presentation.view.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.shop.open.R
import com.tokopedia.shop.open.shop_open_revamp.common.PageNameConstant
import com.tokopedia.shop.open.shop_open_revamp.listener.FragmentNavigationInterface
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_shop_open_revamp_finish.*

class ShopOpenRevampFinishFragment : Fragment() {

    private val handler = Handler()
    lateinit var fragmentNavigationInterface: FragmentNavigationInterface
    private lateinit var lottieAnimationView: LottieAnimationView

    private val userSession: UserSessionInterface by lazy {
        UserSession(activity)
    }

    companion object {
        const val LOTTIE_ANIMATION = "https://ecs7.tokopedia.net/android/lottie/merchant/others/REV_Merchant_Onboarding_lottie.json"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentNavigationInterface = context as FragmentNavigationInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shop_open_revamp_finish, container, false)
        lottieAnimationView = view.findViewById(R.id.lottie_success_create_shop)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shopId = userSession.shopId
        setupAnimation(view, shopId)
        val greetingText = getString(R.string.open_shop_revamp_text_title_finish_success, userSession.name)
        txt_greeting.text = greetingText
    }

    private fun setupAnimation(view: View, shopId: String) {
        context?.let {
            val lottieCompositionLottieTask = LottieCompositionFactory.fromUrl(it, LOTTIE_ANIMATION)

            lottieCompositionLottieTask.addListener { result ->
                lottieAnimationView.setComposition(result)
                lottieAnimationView.visibility = View.VISIBLE
                lottieAnimationView.playAnimation()
                lottieAnimationView.repeatCount = LottieDrawable.INFINITE

                handler.postDelayed({
                    activity?.finish()
                    RouteManager.route(context, ApplinkConst.SHOP, shopId)
                }, 3000)
            }
        }
    }

}