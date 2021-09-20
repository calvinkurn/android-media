package com.tokopedia.createpost.view.util;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.tokopedia.createpost.createpost.R;

public class TempTagViewProvider {
    static float dX, dY;

    /*

    val tagImg = findViewById<ConstraintLayout>(R.id.tag)

        tagImg.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        val vv = TagViewProvider.getTagView(this@MainActivity)
//                        vv.x = event.x
//                        vv.y = event.y
//                        tagImg.addView(vv)
                        TagViewProvider.addViewToParent(vv, v as ConstraintLayout, event)
                    }

                }

                return v?.onTouchEvent(event) ?: true
            }
        })

     */

    static View getTagView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.temp_view_tag, null);
        TextView tv = (TextView) view.findViewById(R.id.slase_price);
        tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        return view;
    }

    static void addViewToParent(View child, ConstraintLayout parent, MotionEvent event) {
        child.setVisibility(View.INVISIBLE);

        child.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float gotoX;
                        if ((event.getRawX() + dX) + pxFromDp(child.getContext(), 145) > parent.getRight()) {
//                            gotoX = (event.getRawX() + dX) - pxFromDp(child.getContext(), 145);
                            gotoX = parent.getRight() - pxFromDp(child.getContext(), 145); /*Blocking right on drag*/
                        } else if ((event.getRawX() + dX) < 0) {
                            gotoX = 0; /*Blocking left on drag*/
                        } else {
                            gotoX = event.getRawX() + dX; /*Normal horizontal drag*/
                        }

                        float gotoY;
                        if ((event.getY()) - pxFromDp(child.getContext(), 68) < 0) {
                            gotoY = 0; /*Blocking top on drag*/
                        } else if (((event.getRawY() + dX) + pxFromDp(child.getContext(), 68) > parent.getBottom())) {
                            gotoY = parent.getBottom() - pxFromDp(child.getContext(), 68);  /*Blocking bottom on drag*/
                        } else {
                            gotoY = event.getRawY() + dY; /*Normal vertical drag*/
                        }


                        child.animate()
                                .x(gotoX)
                                .y(gotoY)
                                .setDuration(0)
                                .start();

                        if (view.getY() < parent.getHeight() * 0.75) {
                            view.findViewById(R.id.topNotch).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.bottomNotch).setVisibility(View.GONE);
                        } else {
                            view.findViewById(R.id.topNotch).setVisibility(View.GONE);
                            view.findViewById(R.id.bottomNotch).setVisibility(View.VISIBLE);
                        }

                        return true;
                }
                return false;
            }
        });

        /*Handling for X position*/
        float xTapped = event.getX() - pxFromDp(child.getContext(), 145) / 2;
        float x2Want = xTapped + pxFromDp(child.getContext(), 145);
        float x2Diff = parent.getRight() - x2Want;
        if (x2Diff < 0) {
            xTapped += x2Diff;
        }


        /*Handling for Y position*/
        float yTapped = event.getY();
        float y2Want = yTapped + pxFromDp(child.getContext(), 68);
        float y2Diff = parent.getBottom() - y2Want;
        if (y2Diff < 0) {
            yTapped += y2Diff;
        }

        //Handling for negative X axis
        if (xTapped < 0) {
            child.setX(0);
        } else {
            child.setX(xTapped);
        }

        child.setY(yTapped);
        child.setVisibility(View.VISIBLE);
        parent.addView(child);

        if (child.getY() < parent.getHeight() * 0.75) {
            child.findViewById(R.id.topNotch).setVisibility(View.VISIBLE);
            child.findViewById(R.id.bottomNotch).setVisibility(View.GONE);
        } else {
            child.findViewById(R.id.topNotch).setVisibility(View.GONE);
            child.findViewById(R.id.bottomNotch).setVisibility(View.VISIBLE);
        }

//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                ((Activity)parent.getContext()).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        /*Handling for X position*/
//                        float xTapped = event.getX() - temp.getWidth() / 2;
//                        float x2Want = xTapped + temp.getWidth();
//                        float x2Diff = parent.getRight() - x2Want;
//                        if (x2Diff < 0) {
//                            xTapped += x2Diff;
//                        }
//
//
//                        /*Handling for Y position*/
//                        float yTapped = event.getY();
//                        float y2Want = yTapped + temp.getHeight();
//                        float y2Diff = parent.getBottom() - y2Want;
//                        if (y2Diff < 0) {
//                            yTapped += y2Diff;
//                        }
//
//                        child.setX(xTapped);
//                        child.setY(yTapped);
//                        child.setVisibility(View.VISIBLE);
//                        parent.addView(child);
//                        parent.removeView(temp);
//                    }
//                });
//            }
//        });
    }


    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
