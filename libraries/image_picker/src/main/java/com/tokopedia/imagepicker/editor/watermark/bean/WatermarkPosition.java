package com.tokopedia.imagepicker.editor.watermark.bean;

import androidx.annotation.FloatRange;

/**
 * It's a class for saving the position of watermark.
 * Can be used for a single image/text or a set of
 * images/texts.
 *
 * @author huangyz0918 (huangyz0918@gmail.com)
 * @since 29/08/2018
 */
public class WatermarkPosition {

    private double positionX;
    private double positionY;
    private double rotation;

    /**
     * Constructors for WatermarkImage
     */
    public WatermarkPosition(@FloatRange(from = 0, to = 1) double positionX,
                             @FloatRange(from = 0, to = 1) double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public WatermarkPosition(@FloatRange(from = 0, to = 1) double positionX,
                             @FloatRange(from = 0, to = 1) double positionY,
                             double rotation) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.rotation = rotation;
    }

    /**
     * Getters and Setters for those attrs.
     */
    public double getPositionX() {
        return positionX;
    }

    public WatermarkPosition setPositionX(double positionX) {
        this.positionX = positionX;
        return this;
    }

    public double getPositionY() {
        return positionY;
    }

    public WatermarkPosition setPositionY(double positionY) {
        this.positionY = positionY;
        return this;
    }

    public double getRotation() {
        return rotation;
    }

    public WatermarkPosition setRotation(double rotation) {
        this.rotation = rotation;
        return this;
    }
}