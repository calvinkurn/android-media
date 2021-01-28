package com.tokopedia.devicefingerprint.submitdevice.model

class Screen {
    var realWidth = 0
    var realHeight = 0
    var densityDpi = 0
    var xdpi = 0f
    var ydpi = 0f

    constructor() {}

    constructor(realWidth: Int, realHeight: Int) {
        this.realWidth = realWidth
        this.realHeight = realHeight
    }

    constructor(realWidth: Int, realHeight: Int, densityDpi: Int, xdpi: Float, ydpi: Float) {
        this.realWidth = realWidth
        this.realHeight = realHeight
        this.densityDpi = densityDpi
        this.xdpi = xdpi
        this.ydpi = ydpi
    }

}
