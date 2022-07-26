import com.tokopedia.topads.edit.view.model.EditFormDefaultViewModelTest
import com.tokopedia.topads.edit.view.model.KeywordAdsViewModelTest
import com.tokopedia.topads.edit.view.model.ProductAdsListViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    EditFormDefaultViewModelTest::class,
    KeywordAdsViewModelTest::class,
    ProductAdsListViewModelTest::class
)
class TopAdsEditSuite