import android.content.Context
import com.tokopedia.user.session.UserSession

class UserSessionStub(context: Context) : UserSession(context) {
    override fun isLoggedIn(): Boolean {
        return true
    }
}
