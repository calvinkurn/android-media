import React from 'react'
import { View, TouchableNativeFeedback, Text, StyleSheet } from 'react-native'

const TKPPrimaryBtn = ({ type, content, onTap }) => {
  return (
    <TouchableNativeFeedback
      onPress={onTap}>
      <View style={btnStyle[type]}>
        <Text style={btnTextStyle[type]}>{content}</Text>
      </View>
    </TouchableNativeFeedback>
  )
}

const btnStyle = StyleSheet.create({
  small: {

  },
  medium: {
    height: 40,
    backgroundColor: '#ff5722',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 3,
    borderWidth: 1,
    borderColor: '#ff5722',
  },
  big: {
    height: 52,
    backgroundColor: '#ff5722',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 3,
    borderWidth: 1,
    borderColor: '#ff5722',
  },
  extraBigNormal: {
    height: 52,
    backgroundColor: '#ff5722',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 3,
    borderWidth: 1,
    borderColor: '#ff5722',
  },
})

const btnTextStyle = StyleSheet.create({
  small: {

  },
  medium: {
    color: '#f1f1f1',
    fontSize: 13,
    fontWeight: '600'
  },
  big: {
    color: '#f1f1f1',
    fontSize: 14,
    fontWeight: '600',
  },
  extraBigNormal: {
    color: '#f1f1f1',
    fontSize: 20,
  },
})


export default TKPPrimaryBtn