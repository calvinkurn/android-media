package ai.advance.liveness.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LivenessData(
	@SerializedName("is_success_register")
	@Expose
	var isSuccessRegister: Boolean? = null,

	@SerializedName("list_retake")
	@Expose
	var listRetake: ArrayList<Int>? = null,

	@SerializedName("list_message")
	@Expose
	var listMessage: ArrayList<String>? = null,

	@SerializedName("apps")
	@Expose
	var apps: LivenessAppsModel? = null
)
