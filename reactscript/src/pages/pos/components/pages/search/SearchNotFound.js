import React from 'react'
import { View, Text, StyleSheet } from 'react-native'

const NotFoundScreen = () => {
  return (<View style={styles.container}>
    <Text style={styles.title}>Oops... hasil pencarian Anda tidak dapat detemukan</Text>
    <Text tyle={styles.subTitle}>Silakan melakukan pencarian kembali dengan menggunakan kata kunci lain.</Text>
  </View>)
}

export default NotFoundScreen

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingHorizontal: '10%',
    alignItems: 'center',
    top: '25%',
  },
  title: {
    fontSize: 24,
    fontWeight: "600",
    color: 'black',
  },
  subTitle: {
    fontSize: 18,
  }
})