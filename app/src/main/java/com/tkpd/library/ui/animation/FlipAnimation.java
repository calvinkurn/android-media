package com.tkpd.library.ui.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class FlipAnimation extends Animation
{
	private Camera camera;

	private View fromView;
	private View toView;
	private float centerX;
	private float centerY;
	private static long mDuration = 300;
	private boolean forward = true;


	/**
	 * Creates a 3D flip animation between two views.
	 *
	 * @param fromView First view in the transition.
	 * @param toView Second view in the transition.
	 */
	public FlipAnimation(View fromView, View toView, long duration)
	{
		this.fromView = fromView;
		this.toView = toView;
		setDuration(duration);
		setFillAfter(false);
		setInterpolator(new AccelerateDecelerateInterpolator());
	}

	public FlipAnimation(View fromView, View toView)
	{
		this(fromView, toView, mDuration);
	}


	public void reverse()
	{
		forward = false;
		View switchView = toView;
		toView = fromView;
		fromView = switchView;
	}

	@Override
	public void initialize(int width, int height, int parentWidth, int parentHeight)
	{
		super.initialize(width, height, parentWidth, parentHeight);
		centerX = width/2;
		centerY = height/2;
		camera = new Camera();
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t)
	{
		final double radians = Math.PI * interpolatedTime;
		float degrees = (float) (180.0 * radians / Math.PI);

		if (interpolatedTime >= 0.5f)
		{
			degrees -= 180.f;
			fromView.setVisibility(View.GONE);
			toView.setVisibility(View.VISIBLE);
		}

		if (forward)
			degrees = -degrees; //determines direction of rotation when flip begins

		final Matrix matrix = t.getMatrix();
		camera.save();
		camera.translate(0, 0, Math.abs(degrees)*2);
		camera.getMatrix(matrix);
		camera.rotateY(degrees);
		camera.getMatrix(matrix);
		camera.restore();
		matrix.preTranslate(-centerX, -centerY);
		matrix.postTranslate(centerX, centerY);
	}
}
