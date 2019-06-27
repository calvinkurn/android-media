package com.tokopedia.core.var;

import android.support.annotation.IntDef;

import static com.tokopedia.core.var.TokoCashTypeDef.TOKOCASH_ACTIVE;
import static com.tokopedia.core.var.TokoCashTypeDef.TOKOCASH_INACTIVE;


/**
 * Created by nabillasabbaha on 11/15/17.
 */

@Deprecated
@IntDef({TOKOCASH_ACTIVE, TOKOCASH_INACTIVE})
public @interface TokoCashTypeDef {
    int TOKOCASH_ACTIVE = 1;
    int TOKOCASH_INACTIVE = 0;
}