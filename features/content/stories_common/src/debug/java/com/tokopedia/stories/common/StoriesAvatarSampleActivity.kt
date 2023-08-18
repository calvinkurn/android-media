package com.tokopedia.stories.common

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.stories.common.databinding.ActivityStoriesAvatarSampleBinding
import com.tokopedia.stories.common.domain.StoriesKey

/**
 * Created by kenny.hadisaputra on 24/07/23
 */
class StoriesAvatarSampleActivity : BaseActivity() {

    private val binding by lazy {
        ActivityStoriesAvatarSampleBinding.inflate(layoutInflater)
    }

    private val storiesManager by activityStoriesManager(StoriesKey.ShopPage)

    private val adapter by lazy {
        StoriesAvatarSampleAdapter(storiesManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        binding.btnToggle.setOnClickListener {
//            binding.storiesAvatar.setImageUrl("https://4.img-dpreview.com/files/p/E~TS590x0~articles/3925134721/0266554465.jpeg")
//        }
//
//        binding.storiesAvatar.setOnClickListener {
//            Toast.makeText(this, "Opening Not Detail", Toast.LENGTH_SHORT).show()
//        }

        binding.rvAvatar.adapter = adapter

        val shopIds = List(20) { it.toString() }
        adapter.submitList(shopIds)

        storiesManager.updateStories(shopIds)
    }
}
