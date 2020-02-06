package ai.advance.liveness.data.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody

data class LivenessScoreRequest(
	@SerializedName("project_id")
	@Expose
	val projectId: RequestBody,

	@SerializedName("params")
	@Expose
	val params: RequestBody,

	@SerializedName("ktp_image")
	@Expose
	val ktpImage: MultipartBody.Part,

	@SerializedName("face_Image")
	@Expose
	val faceImage: MultipartBody.Part
)
