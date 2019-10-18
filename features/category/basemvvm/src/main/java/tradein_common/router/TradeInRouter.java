package tradein_common.router;

import android.content.Context;
import android.content.Intent;

public interface TradeInRouter {
    Intent getKYCIntent(Context context,int projectId);
}

