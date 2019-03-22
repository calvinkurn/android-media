package tradein_common.router;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.transaction.common.sharedata.AddToCartRequest;
import com.tokopedia.transaction.common.sharedata.AddToCartResult;

import rx.Observable;

public interface TradeInRouter {
    Intent getCheckoutIntent(Context context, String deviceid);

    Intent getKYCIntent(Context context,int projectId);

    Observable<AddToCartResult> addToCartProduct(AddToCartRequest addToCartRequest, boolean isOneClickShipment);
}

