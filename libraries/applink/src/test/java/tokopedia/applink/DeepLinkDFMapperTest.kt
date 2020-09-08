package tokopedia.applink

import android.content.Context
import android.os.Build
import com.tokopedia.applink.DeeplinkDFMapper
import com.tokopedia.applink.internal.ApplinkConstInternalCategory
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkObject
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Field
import java.lang.reflect.Modifier

class DeepLinkDFMapperTest {

    @RelaxedMockK
    lateinit var context: Context

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(DeeplinkDFMapper)
        setFinalStatic(Build.VERSION::class.java.getField("SDK_INT"), 28)
    }

    @Test
    fun `check applink onboarding, should return DF_BASE`() {
        val DF_BASE = "df_category_trade_in"
        every { DeeplinkDFMapper.getDFDeeplinkIfNotInstalled(context, ApplinkConstInternalCategory.TRADEIN) }
    }

    @Throws(Exception::class)
    fun setFinalStatic(field: Field, newValue: Any?) {
        field.isAccessible = true
        val modifiersField: Field = Field::class.java.getDeclaredField("modifiers")
        modifiersField.isAccessible = true
        modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())
        field.set(null, newValue)
    }
}