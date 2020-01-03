package com.tokopedia.tokopedia.feedplus

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import org.spekframework.spek2.dsl.GroupBody
import org.spekframework.spek2.dsl.Root
import org.spekframework.spek2.style.gherkin.FeatureBody

class InstantTaskExecutorRuleSpek(root: Root) {
    init {
        root.beforeGroup {
            ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
                override fun executeOnDiskIO(runnable: Runnable) {
                    runnable.run()
                }

                override fun isMainThread(): Boolean {
                    return true
                }

                override fun postToMainThread(runnable: Runnable) {
                    runnable.run()
                }
            })
        }

        root.afterGroup {
            ArchTaskExecutor.getInstance().setDelegate(null)
        }
    }
}