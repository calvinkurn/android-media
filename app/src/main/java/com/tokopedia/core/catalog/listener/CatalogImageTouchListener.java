package com.tokopedia.core.catalog.listener;

import android.view.MotionEvent;
import android.view.View;

/**
 * @author anggaprasetiyo on 10/20/16.
 */

public class CatalogImageTouchListener implements View.OnTouchListener {
    private float startX;
    private float startY;
    private IActionTouch actionTouch;

    public CatalogImageTouchListener(IActionTouch actionTouch) {
        this.actionTouch = actionTouch;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getRawX();
                startY = event.getRawY();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                float currX = event.getRawX();
                float currY = event.getRawY();
                float deltaX = currX - startX;
                float deltaY = currY - startY;
                System.out.println("Delta X UP: " + deltaX);
                System.out.println("Delta Y UP: " + deltaY);
                if (deltaX < 20 && deltaX > -20) {
                    if (deltaY < 20 && deltaY > -20) {
                        actionTouch.onCatalogImageClicked();
                    }
                }
                v.getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_MOVE:
                currX = event.getRawX();
                currY = event.getRawY();
                deltaY = currY - startY;
                if (deltaY < 20 && deltaY > -20) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            default:
                break;
        }
        return false;
    }

    public interface IActionTouch {
        void onCatalogImageClicked();
    }
}
