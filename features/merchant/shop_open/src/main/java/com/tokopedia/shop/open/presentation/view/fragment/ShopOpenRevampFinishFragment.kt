package com.tokopedia.shop.open.presentation.view.fragment

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
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.shop.open.R
import com.tokopedia.shop.open.analytic.ShopOpenRevampTracking
import com.tokopedia.shop.open.common.EspressoIdlingResource
import com.tokopedia.shop.open.common.ScreenNameTracker
import com.tokopedia.shop.open.listener.FragmentNavigationInterface
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class ShopOpenRevampFinishFragment : Fragment() {

    private val handler = Handler()
    private lateinit var lottieAnimationView: LottieAnimationView
    private var shopOpenRevampTracking: ShopOpenRevampTracking? = null
    private var fragmentNavigationInterface: FragmentNavigationInterface? = null
    private lateinit var loading: LoaderUnify
    private var txtGreeting: Typography? = null

    private val userSession: UserSessionInterface by lazy {
        UserSession(activity)
    }

    companion object {
        const val LOTTIE_ANIMATION = TokopediaImageUrl.LOTTIE_ANIMATION
        private const val LOTTIE_ANIMATION_DURATION = 3000L
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentNavigationInterface = context as FragmentNavigationInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            shopOpenRevampTracking = ShopOpenRevampTracking()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shop_open_revamp_finish, container, false)
        lottieAnimationView = view.findViewById(R.id.lottie_success_create_shop)
        loading = view.findViewById(R.id.loading)
        txtGreeting = view.findViewById(R.id.txt_greeting)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loading.visibility = View.VISIBLE
        val shopId = userSession.shopId
        val fullName = userSession.name
        val firstName = fullName.split(" ")[0]

        setupAnimation(shopId)
        shopOpenRevampTracking?.sendScreenNameTracker(ScreenNameTracker.SCREEN_CONGRATULATION)
        val greetingText = getString(R.string.open_shop_revamp_text_title_finish_success, firstName)
        txtGreeting?.text = greetingText
    }

    private fun setupAnimation(shopId: String) {
        context?.let {
            val lottieCompositionLottieTask = LottieCompositionFactory.fromUrl(it, LOTTIE_ANIMATION)

            lottieCompositionLottieTask.addListener { result ->
                lottieAnimationView.setComposition(result)
                loading.visibility = View.GONE
                lottieAnimationView.visibility = View.VISIBLE
                lottieAnimationView.playAnimation()
                lottieAnimationView.repeatCount = LottieDrawable.INFINITE

                handler.postDelayed({
                    activity?.let {
                        it.finish()
                        val intent = RouteManager.getIntent(context, ApplinkConst.SHOP, shopId)
                        intent.putExtra(ApplinkConstInternalMarketplace.PARAM_FIRST_CREATE_SHOP, true)
                        startActivity(intent)
                        EspressoIdlingResource.decrement()
                    }
                }, LOTTIE_ANIMATION_DURATION)
            }
        }
    }
}
