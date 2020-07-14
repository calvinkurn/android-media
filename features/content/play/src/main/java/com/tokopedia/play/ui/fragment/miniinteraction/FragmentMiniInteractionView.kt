package com.tokopedia.play.ui.fragment.miniinteraction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.view.fragment.PlayMiniInteractionFragment

/**
 * Created by jegul on 06/05/20
 */
class FragmentMiniInteractionView(
        private val channelId: String,
        container: ViewGroup,
        private val fragmentManager: FragmentManager
) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_fragment_mini_interaction, container, true)
                    .findViewById(R.id.fl_mini_interaction)

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    internal fun init() {
        fragmentManager.findFragmentByTag(MINI_INTERACTION_FRAGMENT_TAG) ?: PlayMiniInteractionFragment.newInstance(channelId).also {
            fragmentManager.beginTransaction()
                    .replace(view.id, it, MINI_INTERACTION_FRAGMENT_TAG)
                    .commit()
        }
    }

    internal fun release() {
        fragmentManager.findFragmentByTag(MINI_INTERACTION_FRAGMENT_TAG)?.let { fragment ->
            fragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit()
        }
    }

    companion object {
        private const val MINI_INTERACTION_FRAGMENT_TAG = "fragment_mini_interaction"
    }
}