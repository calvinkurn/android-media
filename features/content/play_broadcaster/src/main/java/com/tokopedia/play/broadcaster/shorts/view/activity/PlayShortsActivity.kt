package com.tokopedia.play.broadcaster.shorts.view.activity

import android.os.Bundle
import android.view.LayoutInflater
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.play.broadcaster.databinding.ActivityPlayShortsBinding

/**
 * Created By : Jonathan Darwin on November 02, 2022
 */
@Suppress("LateinitUsage")
class PlayShortsActivity : BaseActivity() {

    private lateinit var binding: ActivityPlayShortsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayShortsBinding.inflate(
            LayoutInflater.from(this),
            null,
            false
        )
        setContentView(binding.root)
    }
}
