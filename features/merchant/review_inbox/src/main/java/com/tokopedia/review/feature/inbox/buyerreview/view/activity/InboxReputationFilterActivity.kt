package com.tokopedia.review.feature.inbox.buyerreview.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.design.text.TextDrawable
import com.tokopedia.review.feature.inbox.buyerreview.view.fragment.InboxReputationFilterFragment
import com.tokopedia.review.feature.inbox.buyerreview.view.fragment.InboxReputationFragment
import com.tokopedia.review.inbox.R

/**
 * @author by nisie on 8/21/17.
 */
class InboxReputationFilterActivity : BaseSimpleActivity() {

    interface ResetListener {
        fun resetFilter()
    }

    var listener: ResetListener? = null

    override fun getNewFragment(): Fragment {
        val timeFilter: String? =
            intent.getStringExtra(InboxReputationFilterFragment.SELECTED_TIME_FILTER)
        val statusFilter: String? =
            intent.getStringExtra(InboxReputationFilterFragment.SELECTED_SCORE_FILTER)
        val tab: Int = intent.getIntExtra(InboxReputationFragment.PARAM_TAB, -1)
        val fragment: Fragment =
            InboxReputationFilterFragment.createInstance(timeFilter, statusFilter, tab)
        listener = fragment as InboxReputationFilterFragment?
        return fragment
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(Menu.NONE, R.id.action_reset, 0, "")
        val menuItem: MenuItem = menu.findItem(R.id.action_reset)
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuItem.icon = resetMenu
        return true
    }

    private val resetMenu: Drawable
        get() {
            val drawable = TextDrawable(this)
            drawable.text = resources.getString(R.string.reset_title)
            drawable.setTextColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
            return drawable
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if ((item.itemId == R.id.action_reset && listener != null)) {
            listener?.resetFilter()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        toolbar.setPadding(PADDING_ZERO, PADDING_ZERO, PADDING_20, PADDING_ZERO)
    }

    companion object {
        const val PADDING_20 = 20
        const val PADDING_ZERO = 0

        fun createIntent(
            context: Context?, timeFilter: String?,
            scoreFilter: String?,
            tab: Int
        ): Intent {
            val intent = Intent(context, InboxReputationFilterActivity::class.java)
            intent.putExtra(
                InboxReputationFilterFragment.SELECTED_TIME_FILTER,
                timeFilter
            )
            intent.putExtra(
                InboxReputationFilterFragment.SELECTED_SCORE_FILTER,
                scoreFilter
            )
            intent.putExtra(InboxReputationFragment.PARAM_TAB, tab)
            return intent
        }
    }
}