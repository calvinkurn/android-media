import React from 'react'
import ReactNative, { StyleSheet } from 'react-native'

export const Text = ({style, ...props}) => {
    return <ReactNative.Text style={[styles.font, style]} {...props} />;
}

const styles = StyleSheet.create({
    font: {
        fontFamily: 'Roboto-Regular'
    }
}) 