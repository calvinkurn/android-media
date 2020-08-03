package com.tokopedia.play.ui.fragment.userinteraction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.view.fragment.PlayUserInteractionFragment
import com.tokopedia.play.view.fragment.PlayVideoFragment

/**
 * Created by jegul on 05/05/20
 */
class FragmentUserInteractionView(
        private val channelId: String,
        container: ViewGroup,
        private val fragmentManager: FragmentManager
) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_fragment_user_interaction, container, true)
                    .findViewById(R.id.fl_user_interaction)

    init {
        fragmentManager.findFragmentByTag(USER_INTERACTION_FRAGMENT_TAG) ?: getPlayUserInteractionFragment().also {
            fragmentManager.beginTransaction()
                    .replace(view.id, it, USER_INTERACTION_FRAGMENT_TAG)
                    .commit()
        }
    }

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    private fun getPlayUserInteractionFragment(): Fragment {
        val fragmentFactory = fragmentManager.fragmentFactory
        return fragmentFactory.instantiate(container.context.classLoader, PlayUserInteractionFragment::class.java.name).apply {
            arguments = Bundle().apply {
                putString(PLAY_KEY_CHANNEL_ID, channelId)
            }
        }
    }

    companion object {
        private const val USER_INTERACTION_FRAGMENT_TAG = "fragment_interaction"
    }
}