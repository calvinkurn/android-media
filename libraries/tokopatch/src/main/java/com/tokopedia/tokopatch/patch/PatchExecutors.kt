package com.tokopedia.tokopatch.patch

import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.Log
import com.meituan.robust.ChangeQuickRedirect
import com.meituan.robust.PatchExecutor
import com.meituan.robust.PatchesInfo
import com.tokopedia.tokopatch.model.Patch
import dalvik.system.DexClassLoader
import java.io.File
import java.lang.reflect.Field
import java.util.*

/**
 * Author errysuprayogi on 11,June,2020
 */
class PatchExecutors(
    context: Context,
    patches: List<Patch>,
    callBack: PatchCallBack
) : Thread() {
    private val context: Context = context.applicationContext
    private val callBack: PatchCallBack
    private val patchList: List<Patch>
    override fun run() {
        try {
            applyPatchList(patchList)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val ROBUST_PATCH_CACHE_DIR = "patch_"
        private const val TAG = "robust"
    }

    init {
        this.callBack = callBack
        patchList = patches
    }

    private fun applyPatchList(patches: List<Patch>?) {
        if (null == patches || patches.isEmpty()) {
            return
        }
        Log.d("robust", " patchManipulate list size is " + patches.size)
        for (p in patches) {
            if (p.isAppliedSuccess) {
                Log.d("robust", "p.isAppliedSuccess() skip ${p.tempPath}")
                continue
            }
            var currentPatchResult = false
            try {
                currentPatchResult = patch(context, p)
            } catch (t: Throwable) {
                callBack.exceptionNotify(
                    context, t,"class:PatchExecutor method:applyPatchList line:69"
                )
            }
            if (currentPatchResult) {
                p.isAppliedSuccess = true
                callBack.onPatchApplied(context, true, p)
            } else {
                callBack.onPatchApplied(context, false, p)
            }
            p.delete()
            Log.d(
                "robust",
                "patch :" + p.name + ", apply result " + currentPatchResult
            )
        }
    }

    private fun patch(
        context: Context,
        patch: Patch
    ): Boolean {
        val classLoader: ClassLoader
        try {
            val dexOutputDir: File = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                context.codeCacheDir
            } else {
                getPatchCacheDirPath(context,patch.version + patch.md5)
            }
            classLoader = DexClassLoader(
                patch.tempPath, dexOutputDir.absolutePath,
                null, PatchExecutor::class.java.classLoader
            )
            var patchClass: Class<*>
            var sourceClass: Class<*>
            val patchesInfoClass: Class<*>
            var patchesInfo: PatchesInfo? = null
            try {
                Log.d(
                    TAG,
                    "patch patch_info_name:" + patch.patchesInfoImplClassFullName
                )
                patchesInfoClass = classLoader.loadClass(patch.patchesInfoImplClassFullName)
                patchesInfo = patchesInfoClass.newInstance() as PatchesInfo
            } catch (t: Throwable) {
                Log.e(TAG, "patch failed 188 ", t)
            }
            if (patchesInfo == null) {
                callBack.logNotify(
                    context,
                    "patchesInfo is null, patch info:id = ${patch.name},md5 = ${patch.md5}",
                    "class:PatchExecutor method:patch line:114"
                )
                return false
            }

            //classes need to patch
            val patchedClasses =
                patchesInfo.patchedClassesInfo
            if (null == patchedClasses || patchedClasses.isEmpty()) {
                callBack.logNotify(
                    context,
                    "patchedClasses is null or empty, patch info:id = ${patch.name},md5 = ${patch.md5}",
                    "class:PatchExecutor method:patch line:122"
                )
                return false
            }
            var error = false
            for (patchedClassInfo in patchedClasses) {
                val patchedClassName = patchedClassInfo.patchedClassName
                val patchClassName = patchedClassInfo.patchClassName
                if (TextUtils.isEmpty(patchedClassName) || TextUtils.isEmpty(patchClassName)) {
                    callBack.logNotify(
                        context,
                        "patchedClasses or patchClassName is empty, patch info:id = ${patch.name},md5 = ${patch.md5}",
                        "class:PatchExecutor method:patch line:131"
                    )
                    continue
                }
                Log.d(TAG, "current path:$patchedClassName")
                try {
                    try {
                        sourceClass = classLoader.loadClass(patchedClassName.trim { it <= ' ' })
                    } catch (e: ClassNotFoundException) {
                        error = true
                        callBack.exceptionNotify(
                            context,
                            e,
                            "class:PatchExecutor method:patch line:258"
                        )
                        continue
                    }
                    val fields =
                        sourceClass.declaredFields
                    Log.d(
                        TAG,
                        "oldClass :" + sourceClass + " fields " + fields.size
                    )
                    var changeQuickRedirectField: Field? = null
                    for (field in fields) {
                        if (TextUtils.equals(
                                field.type.canonicalName,
                                ChangeQuickRedirect::class.java.canonicalName
                            ) && TextUtils.equals(
                                field.declaringClass.canonicalName,
                                sourceClass.canonicalName
                            )
                        ) {
                            changeQuickRedirectField = field
                            break
                        }
                    }
                    if (changeQuickRedirectField == null) {
                        callBack.logNotify(
                            context,
                            "changeQuickRedirectField  is null, patch info:id = ${patch.name},md5 = ${patch.md5}",
                            "class:PatchExecutor method:patch line:147"
                        )
                        Log.d(
                            TAG,
                            "current path:$patchedClassName something wrong !! can  not find:ChangeQuickRedirect in $patchClassName"
                        )
                        continue
                    }
                    Log.d(
                        TAG,
                        "current path:$patchedClassName find:ChangeQuickRedirect $patchClassName"
                    )
                    try {
                        patchClass = classLoader.loadClass(patchClassName)
                        val patchObject = patchClass.newInstance()
                        changeQuickRedirectField.isAccessible = true
                        changeQuickRedirectField[null] = patchObject
                        Log.d(
                            TAG,
                            "changeQuickRedirectField set success $patchClassName"
                        )
                        callBack.logNotify(
                            context,
                            "changeQuickRedirectField set success",
                            patchClassName
                        )
                    } catch (t: Throwable) {
                        Log.e(TAG, "patch failed! ")
                        callBack.logNotify(context, "patch failed! ", patchClassName)
                        error = true
                        callBack.exceptionNotify(
                            context,
                            t,
                            "class:PatchExecutor method:patch line:163"
                        )
                    }
                } catch (t: Throwable) {
                    Log.e(TAG, "patch failed! ")
                    error = true
                    callBack.exceptionNotify(
                        context,
                        t,
                        "class:PatchExecutor method:patch line:169"
                    )
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cleanUp(
                    File(
                        String.format(
                            "%s/oat",
                            context.cacheDir.absolutePath
                        )
                    )
                )
            } else {
                cleanUp(dexOutputDir)
            }
            Log.d(TAG, "patch finished result " + !error)
            return !error
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
        return false
    }

    private fun cleanUp(fileOrDirectory: File) {
        Log.d("robust", "Cleanup.. ${fileOrDirectory.absolutePath}")
        if (fileOrDirectory.isDirectory) for (child in Objects.requireNonNull(
            fileOrDirectory.listFiles()
        )) cleanUp(child)
        fileOrDirectory.delete()
    }

    private fun getPatchCacheDirPath(
        c: Context,
        key: String
    ): File {
        val patchTempDir = c.getDir(
            ROBUST_PATCH_CACHE_DIR + key,
            Context.MODE_PRIVATE
        )
        if (!patchTempDir.exists()) {
            patchTempDir.mkdir()
        }
        return patchTempDir
    }
}