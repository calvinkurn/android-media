package com.tokopedia.stories.common

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.stories.common.databinding.ActivityStoriesAvatarSampleBinding

/**
 * Created by kenny.hadisaputra on 24/07/23
 */
class StoriesAvatarSampleActivity : BaseActivity() {

    private val binding by lazy {
        ActivityStoriesAvatarSampleBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnToggle.setOnClickListener {
            binding.storiesAvatar.setImageUrl("https://4.img-dpreview.com/files/p/E~TS590x0~articles/3925134721/0266554465.jpeg")
        }
    }
}
