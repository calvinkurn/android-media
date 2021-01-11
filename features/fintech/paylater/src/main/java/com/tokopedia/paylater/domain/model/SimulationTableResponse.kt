
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class SimulationTableResponse(
        val tenureDesc: String,
        val amount: Double,
        val isPopular: Boolean,
        var isSelected: Boolean,
        val installmentData: ArrayList<CreditCardBank>
)
@Parcelize
data class CreditCardBank(
        val bankLogo: String,
        val bankName: String,
        val bankBenefits: String,
        val availableDuration: String
): Parcelable
