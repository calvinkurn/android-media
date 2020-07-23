package com.tokopedia.play.ui.fragment.userinteraction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.view.fragment.PlayUserInteractionFragment

/**
 * Created by jegul on 05/05/20
 */
class FragmentUserInteractionView(
        channelId: String,
        container: ViewGroup,
        fragmentManager: FragmentManager
) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_fragment_user_interaction, container, true)
                    .findViewById(R.id.fl_user_interaction)

    init {
        fragmentManager.findFragmentByTag(USER_INTERACTION_FRAGMENT_TAG) ?: PlayUserInteractionFragment.newInstance(channelId).also {
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

    companion object {
        private const val USER_INTERACTION_FRAGMENT_TAG = "fragment_interaction"
    }
}