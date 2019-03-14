package tradein_common.router;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.transaction.common.sharedata.ShipmentFormRequest;

public interface TradeInRouter {
    Intent getCheckoutIntent(Context context, String deviceid);

    Intent getKYCIntent(Context context,int projectId);
}

