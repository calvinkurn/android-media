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
class InboxReputationFilterActivity constructor() : BaseSimpleActivity() {
    open interface ResetListener {
        fun resetFilter()
    }

    var listener: ResetListener? = null
    override fun getNewFragment(): Fragment? {
        val timeFilter: String? =
            getIntent().getStringExtra(InboxReputationFilterFragment.Companion.SELECTED_TIME_FILTER)
        val statusFilter: String? =
            getIntent().getStringExtra(InboxReputationFilterFragment.Companion.SELECTED_SCORE_FILTER)
        val tab: Int = getIntent().getIntExtra(InboxReputationFragment.Companion.PARAM_TAB, -1)
        val fragment: Fragment =
            InboxReputationFilterFragment.Companion.createInstance(timeFilter, statusFilter, tab)
        listener = fragment as InboxReputationFilterFragment?
        return fragment
    }

    public override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(Menu.NONE, R.id.action_reset, 0, "")
        val menuItem: MenuItem = menu.findItem(R.id.action_reset)
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuItem.setIcon(resetMenu)
        return true
    }

    private val resetMenu: Drawable
        private get() {
            val drawable: TextDrawable = TextDrawable(this)
            drawable.setText(getResources().getString(R.string.reset_title))
            drawable.setTextColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
            return drawable
        }

    public override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if ((item.getItemId() == R.id.action_reset
                    && listener != null)
        ) {
            listener!!.resetFilter()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    override fun setupLayout(savedInstanceState: Bundle) {
        super.setupLayout(savedInstanceState)
        toolbar.setPadding(0, 0, 20, 0)
    }

    companion object {
        fun createIntent(
            context: Context?, timeFilter: String?,
            scoreFilter: String?,
            tab: Int
        ): Intent {
            val intent: Intent = Intent(context, InboxReputationFilterActivity::class.java)
            intent.putExtra(
                InboxReputationFilterFragment.Companion.SELECTED_TIME_FILTER,
                timeFilter
            )
            intent.putExtra(
                InboxReputationFilterFragment.Companion.SELECTED_SCORE_FILTER,
                scoreFilter
            )
            intent.putExtra(InboxReputationFragment.Companion.PARAM_TAB, tab)
            return intent
        }
    }
}