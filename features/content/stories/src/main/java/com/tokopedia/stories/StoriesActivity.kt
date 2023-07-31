package com.tokopedia.stories

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.stories.databinding.ActivityStoriesBinding

class StoriesActivity : AppCompatActivity() {

    private var binding: ActivityStoriesBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
    }

    private fun setupView() {
        binding = ActivityStoriesBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }
}
