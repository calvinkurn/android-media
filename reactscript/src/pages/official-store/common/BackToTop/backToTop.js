import React from 'react'
import { View, TouchableOpacity, StyleSheet, Text, Image } from 'react-native'



const BackToTop = (props) => {
  return (
    <View style={{ alignItems: 'center', }}>
      <TouchableOpacity style={styles.backToTopBtn}
        underlayColor='#fff' onPress={props.onTap}>
        <View style={{ flex: 1, flexDirection: 'row', padding: 10, alignSelf: 'center', justifyContent: 'center', }}>
          <Text style={{ color: 'rgba(0,0,0,.7)', textAlign: 'center', fontSize: 13}}>Kembali ke atas</Text>
          <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_offstore/chevron_thin_up.png' }} style={{width:15, height:10, marginLeft:7, marginTop:3}} />
        </View>
      </TouchableOpacity>
    </View>
  )
}

const styles = StyleSheet.create({
  backToTopBtn: {
    elevation: 4,
    backgroundColor: '#fff',
    borderWidth: 1,
    borderColor: '#fff',
    borderRadius: 25,
    height: 40,
    width: 150,
    alignItems: 'center',
    justifyContent: 'center',
    position: 'absolute',
    bottom: 10,
    shadowColor: "#000000",
    shadowOpacity: 0.31,
    shadowRadius: 2,
    shadowOffset: {
      height: 1,
      width: 0
    }
  }
})

export default BackToTop