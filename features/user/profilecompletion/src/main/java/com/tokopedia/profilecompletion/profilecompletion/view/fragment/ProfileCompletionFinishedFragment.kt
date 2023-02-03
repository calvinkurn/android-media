package com.tokopedia.profilecompletion.profilecompletion.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.profilecompletion.R
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by stevenfredian on 7/3/17.
 */
class ProfileCompletionFinishedFragment : BaseDaggerFragment() {

    private var txtDone: View? = null
    private var userSession: UserSessionInterface? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val parentView = inflater.inflate(R.layout.fragment_profile_completion_finish, container, false)
        initView(parentView)
        setViewListener()
        return parentView
    }

    private fun initView(view: View) {
	txtDone = view.findViewById(R.id.txt_done)
        val imgGender = view.findViewById<ImageView>(R.id.rg_gender)
        imgGender?.loadImage(MAIN_IMG)
    }

    private fun setViewListener() {
	txtDone?.setOnClickListener {
	    if (context != null) {
		userSession = UserSession(context)
		val intent = RouteManager.getIntent(
		    context,
		    userSession?.userId?.let { userId ->
			ApplinkConst.PROFILE.replace(ApplinkConst.Profile.PARAM_USER_ID, userId)
		    }
		)
		intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
		activity?.startActivity(intent)
		activity?.finish()
	    }
	}
    }

    override fun getScreenName(): String = ""
    override fun initInjector() {}

    companion object {
        const val TAG = "finished"
        const val MAIN_IMG = "https://images.tokopedia.net/img/android/user/profilecompletion/profilecompletion_akun_terverifikasi.png"

	fun createInstance(): ProfileCompletionFinishedFragment {
	    return ProfileCompletionFinishedFragment()
	}
    }
}