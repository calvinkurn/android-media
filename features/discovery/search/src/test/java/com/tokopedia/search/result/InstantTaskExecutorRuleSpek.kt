package com.tokopedia.search.result

import android.arch.core.executor.ArchTaskExecutor
import android.arch.core.executor.TaskExecutor
import org.spekframework.spek2.dsl.Root

class InstantTaskExecutorRuleSpek(root: Root) {
    init {
        root.beforeEachTest {
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

        root.afterEachTest {
            ArchTaskExecutor.getInstance().setDelegate(null)
        }
    }
}