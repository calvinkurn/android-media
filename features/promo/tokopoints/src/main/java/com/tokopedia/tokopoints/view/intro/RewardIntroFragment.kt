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
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.rewardintro.TokopediaRewardIntroPage
import kotlinx.android.synthetic.main.tp_layout_reward_intro.*

class RewardIntroFragment : Fragment() {

    companion object {
        fun newInstance(extras: Bundle?): Fragment {
            val fragment = RewardIntroFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tp_layout_reward_intro, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments?.get("intro") as TokopediaRewardIntroPage

        data.imageURL?.let {
            iv_intro_reward.loadImage(data.imageURL)
        }
        tv_intro_title.text = data.title

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_intro_desc.text = Html.fromHtml(data.subtitle, Html.FROM_HTML_MODE_LEGACY)
        } else {
            tv_intro_desc.text = Html.fromHtml(data.subtitle)
        }

        btn_learn.text = data.cTA?.get(0)?.text
        btn_learn.setOnClickListener {
            RouteManager.route(context, data.cTA?.get(0)?.appLink)
        }

        btn_check.text = data.cTA?.get(0)?.text
        btn_check.setOnClickListener {
            RouteManager.route(context, data.cTA?.get(0)?.appLink)
        }
    }

}