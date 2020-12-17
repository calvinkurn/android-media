package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.create.R
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 27/8/20.
 */

class CreationOnboardingFragScreen3 : BaseDaggerFragment() {

    @Inject
    lateinit var userSession: UserSessionInterface

    companion object{
        private const val IMAGE_PATH = "topads_onboard_slide3.png"
    }

    override fun getScreenName(): String {
        return CreationOnboardingFragScreen3::class.java.name
    }

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(resources.getLayout(R.layout.topads_create_onboarding_fragment_layout), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = view.findViewById<TextView>(R.id.title)
        val desc = view.findViewById<TextView>(R.id.desc)
        val dotImage = view.findViewById<ImageButton>(R.id.dotImage)
        val slideImage = view.findViewById<DeferredImageView>(R.id.slideImage)
        title?.text = getString(R.string.topads_create_onboarding_screen_title3)
        desc?.text = getString(R.string.topads_create_onboarding_screen_desc3)
        dotImage?.setImageDrawable(context?.getResDrawable(R.drawable.topads_indi_3))
        slideImage?.loadRemoteImageDrawable(IMAGE_PATH)
    }
}