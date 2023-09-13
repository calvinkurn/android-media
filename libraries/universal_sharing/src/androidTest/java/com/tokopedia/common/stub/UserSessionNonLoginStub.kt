import android.content.Context
import com.tokopedia.user.session.UserSession

class UserSessionNonLoginStub(context: Context) : UserSession(context) {
    override fun isLoggedIn(): Boolean {
        return false
    }
}
