package com.tokopedia.tokopoints.view.intro

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.tokopoints.databinding.TpLayoutRewardIntroBinding
import com.tokopedia.tokopoints.view.model.rewardintro.TokopediaRewardIntroPage
import com.tokopedia.tokopoints.view.util.NetworkDetector
import com.tokopedia.utils.lifecycle.autoClearedNullable

class RewardIntroFragment : Fragment() {

    companion object {
        const val INTRO_KEY = "intro"
        fun newInstance(extras: Bundle?): Fragment {
            val fragment = RewardIntroFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    private var binding by autoClearedNullable<TpLayoutRewardIntroBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = TpLayoutRewardIntroBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.containerMain?.displayedChild = 0
        val data = arguments?.get(INTRO_KEY) as? TokopediaRewardIntroPage

        binding?.apply {
            if (data != null) {
                data.imageURL?.let {
                    ivIntroReward.loadImage(data.imageURL)
                }
                if (!data.title.isNullOrEmpty()) {
                    tvIntroTitle.text = data.title
                }
                if (data.subtitle != null && data.subtitle.isNotEmpty()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvIntroDesc.text = Html.fromHtml(data.subtitle, Html.FROM_HTML_MODE_LEGACY)
                    } else {
                        tvIntroDesc.text = Html.fromHtml(data.subtitle)
                    }
                }
                if (data.cTA?.size != 0) {
                    btnLearn.text = data.cTA?.get(0)?.text
                    btnLearn.setOnClickListener {
                        RouteManager.route(context, data.cTA?.get(0)?.appLink)
                        activity?.finish()
                    }
                    if (data.cTA?.size == 2) {
                        btnCheck.text = data.cTA[1]?.text
                        btnCheck.setOnClickListener {
                            RouteManager.route(context, data.cTA[1]?.appLink)
                            activity?.finish()
                        }
                    }
                }
            } else {
                containerMain.displayedChild = 1
                serverErrorView.showErrorUi(NetworkDetector.isConnectedToInternet(context))
            }
        }
    }
}
