package com.tokopedia.stories.widget

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.stories.widget.databinding.ActivityStoriesWidgetSampleBinding
import com.tokopedia.stories.widget.domain.StoriesKey

/**
 * Created by kenny.hadisaputra on 24/07/23
 */
class StoriesWidgetSampleActivity : BaseActivity() {

    private val binding by lazy {
        ActivityStoriesWidgetSampleBinding.inflate(layoutInflater)
    }

    private val storiesManager by activityStoriesManager(StoriesKey.ShopPage)

    private val adapter by lazy {
        StoriesWidgetSampleAdapter(storiesManager)
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
