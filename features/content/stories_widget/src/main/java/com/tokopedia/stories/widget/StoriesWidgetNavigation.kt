package com.tokopedia.stories.widget

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.applink.RouteManager
import com.tokopedia.stories.internal.getAllStoriesSeenExtra
import com.tokopedia.stories.internal.getStoriesShopId

/**
 * Created by kenny.hadisaputra on 21/08/23
 */
internal class StoriesWidgetNavigation internal constructor(
    lifecycleOwner: LifecycleOwner
) {

    private var mListener: Listener? = null

    private val launcher: ActivityResultLauncher<Intent>

    init {
        launcher = when (lifecycleOwner) {
            is Fragment -> {
                lifecycleOwner.registerForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) {
                    val result = ResultHolder.parse(it)
                    mListener?.onNewResult(result)
                }
            }
            is AppCompatActivity -> {
                lifecycleOwner.registerForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) {
                    val result = ResultHolder.parse(it)
                    mListener?.onNewResult(result)
                }
            }
            else -> error("Only supported for AppCompatActivity and Fragment")
        }
    }

    constructor(fragment: Fragment) : this(fragment as LifecycleOwner)
    constructor(activity: AppCompatActivity) : this(activity as LifecycleOwner)
    fun launch(context: Context, appLink: String) {
        launcher.launch(
            RouteManager.getIntent(context, appLink)
        )
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    data class ResultHolder private constructor(val shopId: String, val isAllStoriesSeen: Boolean) {
        companion object {
            fun parse(activityResult: ActivityResult): ResultHolder {
                val shopId = activityResult.data?.getStoriesShopId().orEmpty()
                val allStoriesSeen = activityResult.data?.getAllStoriesSeenExtra() ?: false
                return ResultHolder(shopId, allStoriesSeen)
            }
        }
    }

    interface Listener {
        fun onNewResult(result: ResultHolder)
    }
}
