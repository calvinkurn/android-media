package com.tokopedia.stories

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.stories.bottomsheet.StoriesThreeDotsBottomSheet
import com.tokopedia.stories.databinding.ActivityStoriesBinding

class StoriesActivity : BaseActivity() {

    private var _binding: ActivityStoriesBinding? = null
    private val binding: ActivityStoriesBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        getData()

        /**
         * For testing purpose
         */

        showBottomSheet()
    }

    private fun setupView() {
        _binding = ActivityStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun getData() {
        val data = intent.data ?: return
        val pathSegment = data.pathSegments
        val shopId = pathSegment[1]
        val storiesId = if (pathSegment.size > 2) pathSegment[2] else ""
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showBottomSheet() {
        binding.button2.setOnClickListener {
            val bottomSheet = StoriesThreeDotsBottomSheet
                .getOrCreateFragment(supportFragmentManager, classLoader)
            bottomSheet.show(supportFragmentManager)
        }
    }
}
