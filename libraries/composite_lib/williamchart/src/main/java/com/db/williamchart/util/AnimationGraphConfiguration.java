package com.db.williamchart.util;

/**
 * Created by normansyahputa on 7/7/17.
 * <p>
 * this class represent {@link com.db.williamchart.view.ChartView} configuration
 * with animation capability.
 */

public interface AnimationGraphConfiguration extends BasicGraphConfiguration {

    int alpha();

    int duration();

    int easingId();

    float overlapFactor();

    float startX();

    float startY();

    Runnable endAnimation();
}
