package com.tokopedia.play.broadcaster.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.itemdecoration.PlayFollowerItemDecoration
import com.tokopedia.play.broadcaster.ui.model.FollowerUiModel
import com.tokopedia.play.broadcaster.util.doOnPreDraw
import com.tokopedia.play.broadcaster.view.adapter.PlayFollowersAdapter

/**
 * Created by jegul on 12/06/20
 */
class PlayShareFollowerView : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val tvShareTitle: TextView
    private val rvFollowers: RecyclerView
    private val tvFollowersCount: TextView

    private val followersAdapter = PlayFollowersAdapter()

    init {
        val view = View.inflate(context, R.layout.view_play_share_follower, this)

        with (view) {
            tvShareTitle = findViewById(R.id.tv_share_title)
            rvFollowers = findViewById(R.id.rv_followers)
            tvFollowersCount = findViewById(R.id.tv_followers_count)
        }

        setupView(view)
    }

    private fun setupView(view: View) {
        rvFollowers.adapter = followersAdapter
        rvFollowers.doOnPreDraw {
            if (rvFollowers.itemDecorationCount == 0)
                rvFollowers.addItemDecoration(PlayFollowerItemDecoration())
        }
    }

    fun setFollowersModel(followers: List<FollowerUiModel>) {
        followersAdapter.setItemsAndAnimateChanges(followers)
    }
}