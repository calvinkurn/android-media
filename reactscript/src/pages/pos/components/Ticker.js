import React from 'react'
import { Text, StyleSheet, View, Image } from 'react-native'

const Ticker = () => {
  return (
    <View style={styles.container}>
      <View>
        <View style={styles.imageWrapper}>
          <Image source={require('./img/toped.png')} style={styles.imageIcon} />
        </View>
      </View>
      <View style={styles.textContainer}>
        <Text style={{ fontSize: 8 }}>Selamat Datang di (Shop name here)</Text>
        <View style={styles.singleLineTextContainer}>
          <Text style={styles.textStyle}>Nikamati Cicilan 0% Gratis Biaya Admin,</Text>
          <Text style={styles.linkText}> Cek Sekarang</Text>
        </View>
      </View>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    borderColor: '#cde4c3',
    borderWidth: 1,
    borderLeftWidth: 3,
    backgroundColor: '#fff',
    alignItems: 'center',
    marginTop: 20,
    width: '90%'
  },
  textContainer: {
    flexDirection: 'column',
    paddingVertical: 10,
    paddingHorizontal: 10,
  },
  singleLineTextContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap'
  },
  textStyle: {
    fontWeight: 'bold',
    fontSize: 10,
  },
  linkText: {
    color: '#42b549',
    fontWeight: 'bold',
    fontSize: 10,
  },
  imageWrapper: {
    paddingHorizontal: 10
  },
  imageIcon: {
    width: 40,
    height: 30,
    resizeMode: 'cover'
  }
})

export default Ticker