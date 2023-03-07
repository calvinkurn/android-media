package com.tokopedia.developer_options.hansel.data

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.tokopedia.developer_options.hansel.uimodel.HanselUiModel

data class HanselPatchData(
    @SerializedName("is_enabled") var isEnabled: Boolean? = null,
    @SerializedName("func_def") var funcDef: FuncDef? = FuncDef(),
    @SerializedName("is_invalid") var isInvalid: String? = null,
    @SerializedName("patch_id") var patchId: Long? = null,
    @SerializedName("patch_name") var patchName: String? = null,
) {
    companion object {
        fun fromJson(json: String): HanselPatchData {
            val data = Gson().fromJson<List<HanselPatchData>>(json, object: TypeToken<List<HanselPatchData>>(){}.type)
            return data.firstOrNull() ?: throw Exception("Hansel Patch data is null")
        }
    }

    fun toHanselUiModel(counter: Int): HanselUiModel {
        return HanselUiModel(
            patchId ?: 0,
            funcDef?.functionId ?: 0,
            funcDef?.unpFunctionName?: "",
            patchName ?: "",
            counter
        )
    }
}

data class FuncDef(
    @SerializedName("unp_function_name") var unpFunctionName: String? = null,
    @SerializedName("package_name") var packageName: String? = null,
    @SerializedName("function_arguments") var functionArguments: ArrayList<String> = arrayListOf(),
    @SerializedName("class_name") var className: String? = null,
    @SerializedName("raw_function_name") var rawFunctionName: String? = null,
    @SerializedName("return_type") var returnType: String? = null,
    @SerializedName("function_id") var functionId: Long? = null,
    @SerializedName("function_name") var functionName: String? = null
)
