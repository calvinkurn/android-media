package com.tokopedia.loginregister

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import org.spekframework.spek2.dsl.GroupBody
import org.spekframework.spek2.dsl.Root

/**
 * Created by Ade Fulki on 2020-01-13.
 * ade.hadian@tokopedia.com
 */

class InstantRunExecutorSpek(root: GroupBody) {
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