package com.tokopedia.catalog.model.util.slidinguppanel.canvassaveproxy;

import android.graphics.Canvas;

public interface CanvasSaveProxy {
    int save();

    boolean isFor(final Canvas canvas);
}
