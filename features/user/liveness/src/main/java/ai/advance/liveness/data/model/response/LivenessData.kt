package ai.advance.liveness.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LivenessData(
	@SerializedName("is_success_register")
	@Expose
	val isSuccessRegister: Boolean? = null,

	@SerializedName("list_retake")
	@Expose
	val listRetake: ArrayList<Int>? = null,

	@SerializedName("list_message")
	@Expose
	val listMessage: ArrayList<String>? = null,

	@SerializedName("apps")
	@Expose
	val apps: LivenessAppsModel? = null
)
